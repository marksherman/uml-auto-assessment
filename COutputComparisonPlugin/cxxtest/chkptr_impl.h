
/*
 * Implementation of Ptr<T> public methods.
 */

// ------------------------------------------------------------------
template <typename T>
Ptr<T>::Ptr() : tag((unsigned long)~0)
{
	pointer = reinterpret_cast<T*>(ChkPtr::__manager.getUninitHandle());
}

// ------------------------------------------------------------------
template <typename T>
Ptr<T>::Ptr(const Ptr<T>& rhs)
{
	pointer = rhs.pointer;
	tag = rhs.tag;

	// When a checked pointer object is copied (due to aliasing or
	// passing to a function), we increment the pointer's reference
	// count if it is live.
	if(ChkPtr::__manager.contains(pointer, tag))
		ChkPtr::__manager.retain(pointer);
}

// ------------------------------------------------------------------
template <typename T>
Ptr<T>::Ptr(T* ptr)
{
	ChkPtr::find_address_results found = ChkPtr::address_not_found;
	unsigned long tagIfFound = 0;

	if(ptr != 0)
	{
		found = ChkPtr::__manager.findAddress(ptr, tagIfFound);
		if(found == ChkPtr::address_not_found)
			ChkPtr::__manager.logError(true, ChkPtr::PTRERR_POINT_TO_NONNEW);
	}

	pointer = ptr;

	if(ptr != 0)
	{
		if(found == ChkPtr::address_found_checked)
			tag = tagIfFound;
		else
			tag = ChkPtr::__manager.moveToChecked(ptr);

		ChkPtr::__manager.retain(pointer);
	}
}

// ------------------------------------------------------------------
template <typename T>
Ptr<T>::~Ptr()
{
	// If the pointer table contains the pointer, then it is still
	// alive and we decrement its reference count. If this causes the
	// count to reach zero, then we have a live pointer going out of
	// scope, which will result in a memory leak.
	if(ChkPtr::__manager.contains(pointer, tag))
	{
		ChkPtr::__manager.release(pointer);

		if(ChkPtr::__manager.getRefCount(pointer) == 0)
			ChkPtr::__manager.logError(false, ChkPtr::PTRERR_LIVE_OUT_OF_SCOPE);
	}
}

// ------------------------------------------------------------------
template <typename T>
Ptr<T>& Ptr<T>::operator=(const Ptr<T>& rhs)
{
	if(this != &rhs)
	{
		// If the pointer to which assignment is being made is alive,
		// decrement its reference count since we are writing over its
		// value. If this causes the count to reach zero, then we have
		// a memory leak because no references to the memory remain.
		if(ChkPtr::__manager.contains(pointer, tag))
		{
			ChkPtr::__manager.release(pointer);
			
			if(ChkPtr::__manager.getRefCount(pointer) == 0)
				ChkPtr::__manager.logError(false, ChkPtr::PTRERR_LIVE_OVERWRITTEN);
		}
		
		pointer = rhs.pointer;
		tag = rhs.tag;

		// Increment the reference count of the pointer that was used
		// on the right-hand side of the assignment.
		if(ChkPtr::__manager.contains(pointer, tag))
			ChkPtr::__manager.retain(pointer);
	}

	return *this;
}

// ------------------------------------------------------------------
template <typename T>
Ptr<T>& Ptr<T>::operator=(T* ptr)
{
	// If the pointer to which assignment is being made is alive,
	// decrement its reference count since we are writing over its
	// value. If this causes the count to reach zero, then we have
	// a memory leak because no references to the memory remain.
	if(ChkPtr::__manager.contains(pointer, tag))
	{
		ChkPtr::__manager.release(pointer);
		
		if(ChkPtr::__manager.getRefCount(pointer) == 0)
			ChkPtr::__manager.logError(false, ChkPtr::PTRERR_LIVE_OVERWRITTEN);
	}
	
	ChkPtr::find_address_results found = ChkPtr::address_not_found;
	unsigned long tagIfFound = 0;

	if(ptr != 0)
	{
		found = ChkPtr::__manager.findAddress(ptr, tagIfFound);
		if(found == ChkPtr::address_not_found)
			ChkPtr::__manager.logError(true, ChkPtr::PTRERR_POINT_TO_NONNEW);
	}
	
	pointer = ptr;
	
	if(ptr != 0)
	{
		if(found == ChkPtr::address_found_checked)
			tag = tagIfFound;
		else
			tag = ChkPtr::__manager.moveToChecked(ptr);

		ChkPtr::__manager.retain(pointer);
	}
	
	return *this;
}

// ------------------------------------------------------------------
template <typename T> template <typename TRhs>
bool Ptr<T>::operator==(const Ptr<TRhs>& rhs)
{
	// If either of the pointers is non-NULL and is not in the pointer
	// table, then it must be a dead pointer. Performing a comparison
	// with a dead pointer can yield to unpredictable results.
	if(isDead() || rhs.isDead())
	{
		// equality checking with dead pointer; depending on the value of a
		// pointer that is no longer alive is unpredictable
		ChkPtr::__manager.logError(false, ChkPtr::PTRERR_DEAD_COMPARISON);
	}
	
	return (pointer == (TRhs*)rhs);
}

// ------------------------------------------------------------------
template <typename T> template <typename TRhs>
bool Ptr<T>::operator==(const TRhs* rhs) const
{
	// If either of the pointers is non-NULL and is not in the pointer
	// table, then it must be a dead pointer. Performing a comparison
	// with a dead pointer can yield to unpredictable results.
	if(isDead())
		ChkPtr::__manager.logError(false, ChkPtr::PTRERR_DEAD_COMPARISON);
	
	return (pointer == rhs);
}

// ------------------------------------------------------------------
template <typename T> template <typename TRhs>
bool Ptr<T>::operator!=(const Ptr<TRhs>& rhs) const
{
	return !(*this == rhs);
}

// ------------------------------------------------------------------
template <typename T> template <typename TRhs>
bool Ptr<T>::operator!=(const TRhs* rhs) const
{
	return !(*this == rhs);
}

// ------------------------------------------------------------------
template <typename T>
T& Ptr<T>::operator*()
{
	return *dereference();
}

// ------------------------------------------------------------------
template <typename T>
T* Ptr<T>::operator->()
{
	return dereference();
}

// ------------------------------------------------------------------
template <typename T>
const T& Ptr<T>::operator*() const
{
	return *constDereference();
}

// ------------------------------------------------------------------
template <typename T>
const T* Ptr<T>::operator->() const
{
	return constDereference();
}

// ------------------------------------------------------------------
template <typename T>
Ptr<T>::operator T*() const
{
	return pointer;
}

// ------------------------------------------------------------------
template <typename T> template <typename TCast>
Ptr<T>::operator Ptr<TCast>() const
{
	return Ptr<TCast>(pointer);
}

// ------------------------------------------------------------------
template <typename T>
T& Ptr<T>::operator[](int index)
{
	if(!isInit())
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_UNINITIALIZED);
	}
	else if(pointer == 0)
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_NULL);
	}
	else if(!ChkPtr::__manager.contains(pointer, tag))
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_FREED);
	}

	if(!ChkPtr::__manager.isArray(pointer))
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_INDEX_NONARRAY);
	}

	size_t size = ChkPtr::__manager.getSize(pointer) / sizeof(T);
	if(index < 0 || index >= (int)size)
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_INDEX_INVALID, index, size - 1);
	}
	
	return *(pointer + index);
}

// ------------------------------------------------------------------
template <typename T>
const T& Ptr<T>::operator[](int index) const
{
	if(!isInit())
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_UNINITIALIZED);
	}
	else if(pointer == 0)
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_NULL);
	}
	else if(!ChkPtr::__manager.contains(pointer, tag))
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_FREED);
	}

	if(!ChkPtr::__manager.isArray(pointer))
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_INDEX_NONARRAY);
	}

	size_t size = ChkPtr::__manager.getSize(pointer) / sizeof(T);
	if(index < 0 || index >= (int)size)
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_INDEX_INVALID, index, size - 1);
	}
	
	return *(pointer + index);
}


/*
 * Implementation of Ptr<T> private methods.
 */

// ------------------------------------------------------------------
template <typename T>
bool Ptr<T>::isInit() const
{
	return pointer !=
		reinterpret_cast<T*>(ChkPtr::__manager.getUninitHandle()); 
}

// ------------------------------------------------------------------
template <typename T>
bool Ptr<T>::isDead() const
{
	return !isInit() || ((pointer != 0) &&
		(!ChkPtr::__manager.contains(pointer, tag)));
}

// ------------------------------------------------------------------
template <typename T>
T* Ptr<T>::dereference()
{
	if(!isInit())
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_UNINITIALIZED);
	}
	else if(pointer == 0)
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_NULL);
	}
	else if(!ChkPtr::__manager.contains(pointer, tag))
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_FREED);
	}

	return pointer;
}

// ------------------------------------------------------------------
template <typename T>
const T* Ptr<T>::constDereference() const
{	
	if(!isInit())
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_UNINITIALIZED);
	}
	else if(pointer == 0)
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_NULL);
	}
	else if(!ChkPtr::__manager.contains(pointer, tag))
	{
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DEREF_FREED);
	}

	return pointer;
}
