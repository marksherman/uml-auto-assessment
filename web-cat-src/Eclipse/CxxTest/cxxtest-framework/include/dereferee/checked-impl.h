/*
 *	This file is part of Dereferee, the diagnostic checked pointer library.
 *
 *	Dereferee is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Dereferee is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Dereferee; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

#define __DMI Dereferee::manager::instance()

namespace Dereferee
{

/*
 * Implementation of checked_ptr public methods.
 */

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>::checked_ptr() :
	pointer(reinterpret_cast<pointer_type>(__DMI->uninit_handle())),
	tag(default_memtag),
	out_of_bounds(false)
{
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>::checked_ptr(const checked_ptr<T>& src) :
	pointer(src.pointer),
	tag(src.tag),
	out_of_bounds(src.out_of_bounds)
{
	switch(src.state())
	{
	case state_alive:
		// When a checked_ptr pointer object is copied (due to aliasing or
		// passing to a function), we increment the pointer's reference
		// count if it is live.
		
		if(__DMI->is_checked(pointer, tag))
			__DMI->retain(pointer);
		
		break;
		
	case state_null:
		break;

	case state_dead_uninitialized:
		__DMI->error(error_assign_dead_uninitialized);
		break;

	case state_dead_deleted:
		__DMI->error(error_assign_dead_deleted);
		break;

	case state_dead_out_of_bounds:
		__DMI->error(error_assign_dead_out_of_bounds);
		break;
	}
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>::checked_ptr(pointer_type ptr) :
	pointer(ptr),
	out_of_bounds(false)
{
	if(ptr != 0)
	{
		bool is_checked;
		mem_info* addr_info = __DMI->address_info(ptr, &is_checked);

		if(!addr_info)
		{
			__DMI->error(error_assign_non_new);
			return;
		}

		store_type_info(addr_info);

		if(is_checked)
		{
			tag = addr_info->tag;
		}
		else
		{
			tag = __DMI->move_to_checked(ptr);
		}

		__DMI->retain(pointer);
	}
	else
	{
		tag = default_memtag;
	}
}

#ifndef DEREFEREE_NO_DYNAMIC_CAST
// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>::checked_ptr(const dynamic_cast_helper<U*>& ptr) :
	pointer((pointer_type)ptr),
	out_of_bounds(false)
{
	if(ptr != 0)
	{
		bool is_checked;
		mem_info* addr_info = __DMI->address_info(ptr, &is_checked);

		if(!addr_info)
		{
			__DMI->error(error_assign_non_new);
			return;
		}

		store_type_info(addr_info);

		if(is_checked)
		{
			tag = addr_info->tag;
		}
		else
		{
			tag = __DMI->move_to_checked(ptr);
		}

		__DMI->retain(pointer);
	}
	else
	{
		tag = default_memtag;
	}
}

// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>::checked_ptr(const dynamic_cast_helper<U* const>& ptr) :
	pointer((pointer_type)ptr),
	out_of_bounds(false)
{
	if(ptr != 0)
	{
		bool is_checked;
		mem_info* addr_info = __DMI->address_info(ptr, &is_checked);

		if(!addr_info)
		{
			__DMI->error(error_assign_non_new);
			return;
		}

		store_type_info(addr_info);

		if(is_checked)
		{
			tag = addr_info->tag;
		}
		else
		{
			tag = __DMI->move_to_checked(ptr);
		}

		__DMI->retain(pointer);
	}
	else
	{
		tag = default_memtag;
	}
}
#endif

#ifndef DEREFEREE_NO_CONST_CAST
// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>::checked_ptr(const const_cast_helper<U*>& ptr) :
	pointer((pointer_type)ptr),
	out_of_bounds(false)
{
	if(ptr != 0)
	{
		bool is_checked;
		mem_info* addr_info = __DMI->address_info(ptr, &is_checked);

		if(!addr_info)
		{
			__DMI->error(error_assign_non_new);
			return;
		}

		store_type_info(addr_info);

		if(is_checked)
		{
			tag = addr_info->tag;
		}
		else
		{
			tag = __DMI->move_to_checked(ptr);
		}

		__DMI->retain(pointer);
	}
	else
	{
		tag = default_memtag;
	}
}

// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>::checked_ptr(const const_cast_helper<U* const>& ptr) :
	pointer((pointer_type)ptr),
	out_of_bounds(false)
{
	if(ptr != 0)
	{
		bool is_checked;
		mem_info* addr_info = __DMI->address_info(ptr, &is_checked);

		if(!addr_info)
		{
			__DMI->error(error_assign_non_new);
			return;
		}

		store_type_info(addr_info);

		if(is_checked)
		{
			tag = addr_info->tag;
		}
		else
		{
			tag = __DMI->move_to_checked(ptr);
		}

		__DMI->retain(pointer);
	}
	else
	{
		tag = default_memtag;
	}
}
#endif

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>::~checked_ptr()
{
	// If the pointer table contains the pointer, then it is still
	// alive and we decrement its reference count. If this causes the
	// count to reach zero, then we have a live pointer going out of
	// scope, which will result in a memory leak.

	if(__DMI->is_checked(pointer, tag))
	{
		__DMI->release(pointer);

		if(__DMI->ref_count(pointer) == 0)
		{
			__DMI->warning(warning_live_pointer_out_of_scope);
		}
	}
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>& checked_ptr<T>::operator=(const checked_ptr<T>& src)
{
	if(this != &src)
	{
		switch(src.state())
		{
		case state_alive:
		case state_null:
			// If the pointer to which assignment is being made is alive,
			// decrement its reference count since we are writing over its
			// value. If this causes the count to reach zero, then we have
			// a memory leak because no references to the memory remain.
			
			if(__DMI->is_checked(pointer, tag))
			{
				__DMI->release(pointer);
				
				if(__DMI->ref_count(pointer) == 0)
				{
					__DMI->warning(warning_live_pointer_overwritten);
				}
			}
			
			pointer = src.pointer;
			tag = src.tag;
			out_of_bounds = src.out_of_bounds;
			
			// Increment the reference count of the pointer that was used
			// on the right-hand side of the assignment.
			
			if(__DMI->is_checked(pointer, tag))
			{
				__DMI->retain(pointer);
			}
			
			break;
			
		case state_dead_uninitialized:
			__DMI->error(error_assign_dead_uninitialized);
			break;

		case state_dead_deleted:
			__DMI->error(error_assign_dead_deleted);
			break;

		case state_dead_out_of_bounds:
			__DMI->error(error_assign_dead_out_of_bounds);
			break;
		}
	}
	
	return *this;
}

#ifndef DEREFEREE_NO_DYNAMIC_CAST
// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>& checked_ptr<T>::operator=(const dynamic_cast_helper<U*>& ptr)
{
	pointer = ptr;
	out_of_bounds = false;

	if(ptr != 0)
	{
		bool is_checked;
		mem_info* addr_info = __DMI->address_info(ptr, &is_checked);

		if(!addr_info)
		{
			__DMI->error(error_assign_non_new);
			return *this;
		}

		store_type_info(addr_info);

		if(is_checked)
		{
			tag = addr_info->tag;
		}
		else
		{
			tag = __DMI->move_to_checked(ptr);
		}

		__DMI->retain(pointer);
	}
	else
	{
		tag = default_memtag;
	}
	
	return *this;
}

// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>& checked_ptr<T>::operator=(
	const dynamic_cast_helper<U* const>& ptr)
{
	pointer = ptr;
	out_of_bounds = false;

	if(ptr != 0)
	{
		bool is_checked;
		mem_info* addr_info = __DMI->address_info(ptr, &is_checked);

		if(!addr_info)
		{
			__DMI->error(error_assign_non_new);
			return *this;
		}

		store_type_info(addr_info);

		if(is_checked)
		{
			tag = addr_info->tag;
		}
		else
		{
			tag = __DMI->move_to_checked(ptr);
		}

		__DMI->retain(pointer);
	}
	else
	{
		tag = default_memtag;
	}
	
	return *this;
}
#endif

#ifndef DEREFEREE_NO_CONST_CAST
// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>& checked_ptr<T>::operator=(const const_cast_helper<U*>& ptr)
{
	pointer = ptr;
	out_of_bounds = false;

	if(ptr != 0)
	{
		bool is_checked;
		mem_info* addr_info = __DMI->address_info(ptr, &is_checked);

		if(!addr_info)
		{
			__DMI->error(error_assign_non_new);
			return *this;
		}

		store_type_info(addr_info);

		if(is_checked)
		{
			tag = addr_info->tag;
		}
		else
		{
			tag = __DMI->move_to_checked(ptr);
		}

		__DMI->retain(pointer);
	}
	else
	{
		tag = default_memtag;
	}
	
	return *this;
}

// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>& checked_ptr<T>::operator=(
	const const_cast_helper<U* const>& ptr)
{
	pointer = ptr;
	out_of_bounds = false;

	if(ptr != 0)
	{
		bool is_checked;
		mem_info* addr_info = __DMI->address_info(ptr, &is_checked);

		if(!addr_info)
		{
			__DMI->error(error_assign_non_new);
			return *this;
		}

		store_type_info(addr_info);

		if(is_checked)
		{
			tag = addr_info->tag;
		}
		else
		{
			tag = __DMI->move_to_checked(ptr);
		}

		__DMI->retain(pointer);
	}
	else
	{
		tag = default_memtag;
	}
	
	return *this;
}
#endif

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator==(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	pointer_state lhs_state = lhs.state();
	pointer_state rhs_state = rhs.state();

	if(lhs_state == state_dead_out_of_bounds ||
			rhs_state == state_dead_out_of_bounds)
	{
		__DMI->error(error_compare_dead_out_of_bounds);
	}
	else if(lhs_state == state_dead_deleted ||
			rhs_state == state_dead_deleted)
	{
		__DMI->error(error_compare_dead_deleted);
	}
	else if(lhs_state == state_dead_uninitialized ||
			rhs_state == state_dead_uninitialized)
	{
		__DMI->error(error_compare_dead_uninitialized);
	}

	// Post error behavior: Do nothing, this is not an unrecoverable error.
	return (lhs.pointer == rhs.pointer);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator!=(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	return !(lhs == rhs);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator<(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	do_relational_check(lhs, rhs);
	return (lhs.pointer < rhs.pointer);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator<=(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	do_relational_check(lhs, rhs);
	return (lhs.pointer <= rhs.pointer);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator>(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	do_relational_check(lhs, rhs);
	return (lhs.pointer > rhs.pointer);
}

// ------------------------------------------------------------------
template <typename U, typename V>
bool operator>=(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	do_relational_check(lhs, rhs);
	return (lhs.pointer >= rhs.pointer);
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>& checked_ptr<T>::operator++()
{
	mem_info* addr_info = __DMI->address_info(pointer);

	pointer++;

	if(!addr_info)
	{
		__DMI->error(error_pointer_not_found);
	}
	else if(!calculating_bounds_checker<value_type>(addr_info)
		.contains(pointer, true))
	{
		out_of_bounds = true;
		__DMI->error(error_arithmetic_moved_out_of_bounds);
	}

	// Post error behavior: Do nothing, this is not an unrecoverable error.
	return *this;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T> checked_ptr<T>::operator++(int)
{
	checked_ptr<T> retval = *this;
	++(*this);
	return retval;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>& checked_ptr<T>::operator--()
{
	mem_info* addr_info = __DMI->address_info(pointer);
	
	pointer--;
	
	if(!addr_info)
	{
		__DMI->error(error_pointer_not_found);
	}
	else if(!calculating_bounds_checker<value_type>(addr_info)
		.contains(pointer, true))
	{
		out_of_bounds = true;
		__DMI->error(error_arithmetic_moved_out_of_bounds);
	}
	
	// Post error behavior: Do nothing, this is not an unrecoverable error.
	return *this;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T> checked_ptr<T>::operator--(int)
{
	checked_ptr<T> retval = *this;
	--(*this);
	return retval;	
}

// ------------------------------------------------------------------
template <typename U>
checked_ptr<U> operator+(const checked_ptr<U>& ptr, ptrdiff_t delta)
{
	mem_info* addr_info = __DMI->address_info(ptr.pointer);
	bool out_of_bounds = false;

	U new_pointer = ptr.pointer + delta;
	
	if(!addr_info)
	{
		__DMI->error(error_pointer_not_found);
	}
	else if(!calculating_bounds_checker<typename checked_ptr<U>::value_type>
		(addr_info).contains(new_pointer, true))
	{
		out_of_bounds = true;
		__DMI->error(error_arithmetic_moved_out_of_bounds);
	}
	
	// Post error behavior: Do nothing, this is not an unrecoverable error.
	checked_ptr<U> result(new_pointer);
	result.out_of_bounds = out_of_bounds;
	return result;
}

// ------------------------------------------------------------------
template <typename U>
checked_ptr<U> operator+(ptrdiff_t delta, const checked_ptr<U>& ptr)
{
	return (ptr + delta);
}

// ------------------------------------------------------------------
template <typename U>
checked_ptr<U> operator-(const checked_ptr<U>& ptr, ptrdiff_t delta)
{
	return (ptr + -delta);
}

// ------------------------------------------------------------------
template <typename U, typename V>
ptrdiff_t operator-(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	pointer_state lhs_state = lhs.state();
	pointer_state rhs_state = rhs.state();
	
	bool lhs_is_null = (lhs_state == state_null);
	bool rhs_is_null = (rhs_state == state_null);

	if(lhs_is_null != rhs_is_null)
	{
		__DMI->error(error_subtraction_one_side_null);
	}
	else
	{
		if(lhs_state == state_dead_out_of_bounds ||
				rhs_state == state_dead_out_of_bounds)
		{
			__DMI->error(error_arithmetic_dead_out_of_bounds);
		}
		else if(lhs_state == state_dead_deleted ||
				rhs_state == state_dead_deleted)
		{
			__DMI->error(error_arithmetic_dead_deleted);
		}
		else if(lhs_state == state_dead_uninitialized ||
				rhs_state == state_dead_uninitialized)
		{
			__DMI->error(error_arithmetic_dead_uninitialized);
		}
		else
		{
			const mem_info* lhs_info = __DMI->address_info(lhs.pointer);
			const mem_info* rhs_info = __DMI->address_info(rhs.pointer);
			
			if(lhs_info != rhs_info)
			{
				__DMI->error(error_subtraction_different_blocks);
			}
		}
	}

	// Post error behavior: Do nothing, this is not an unrecoverable error.
	return (lhs.pointer - rhs.pointer);
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>& checked_ptr<T>::operator+=(ptrdiff_t delta)
{
	mem_info* addr_info = __DMI->address_info(pointer);
	
	pointer += delta;
	
	if(!addr_info)
	{
		__DMI->error(error_pointer_not_found);
	}
	else if(!calculating_bounds_checker<value_type>(addr_info)
		.contains(pointer, true))
	{
		out_of_bounds = true;
		__DMI->error(error_arithmetic_moved_out_of_bounds);
	}
	
	// Post error behavior: Do nothing, this is not an unrecoverable error.
	return *this;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>& checked_ptr<T>::operator-=(ptrdiff_t delta)
{
	return (*this += -delta);
}

// ------------------------------------------------------------------
template <typename T>
typename checked_ptr<T>::reference_type checked_ptr<T>::operator*()
{
	switch(state())
	{
	case state_alive:
		break;

	case state_null:
		__DMI->error(error_deref_null_star_op);
		break;

	case state_dead_uninitialized:
		__DMI->error(error_deref_uninitialized_star_op);
		break;
		
	case state_dead_deleted:
		__DMI->error(error_deref_deleted_star_op);
		break;
		
	case state_dead_out_of_bounds:
		__DMI->error(error_deref_out_of_bounds_star_op);
		break;
	}
	
	const mem_info* addr_info = __DMI->address_info(pointer);
	if(!noncalculating_bounds_checker<T>(addr_info).contains(pointer, false))
	{
		__DMI->error(error_deref_out_of_bounds_star_op);
	}

	// Post error behavior: There is nothing we can do here, we have to
	// return a valid reference. Just let the unrecoverable error occur.
	return *pointer;
}

// ------------------------------------------------------------------
template <typename T>
typename checked_ptr<T>::pointer_type checked_ptr<T>::operator->() const
{
	switch(state())
	{
	case state_alive:
		break;

	case state_null:
		__DMI->error(error_deref_null_arrow_op);
		break;

	case state_dead_uninitialized:
		__DMI->error(error_deref_uninitialized_arrow_op);
		break;
		
	case state_dead_deleted:
		__DMI->error(error_deref_deleted_arrow_op);
		break;
		
	case state_dead_out_of_bounds:
		__DMI->error(error_deref_out_of_bounds_arrow_op);
		break;
	}

	const mem_info* addr_info = __DMI->address_info(pointer);
	if(!noncalculating_bounds_checker<T>(addr_info).contains(pointer, false))
	{
		__DMI->error(error_deref_out_of_bounds_star_op);
	}

	// Post error behavior: There is nothing we can do here, we have to
	// return a valid pointer. Just let the unrecoverable error occur.
	return pointer;
}

// ------------------------------------------------------------------
template <typename T>
checked_ptr<T>::operator pointer_type() const
{
	switch(state())
	{
	case state_alive:
	case state_null:
		break;

	case state_dead_uninitialized:
		__DMI->error(error_used_dead_uninitialized);
		break;
		
	case state_dead_deleted:
		__DMI->error(error_used_dead_deleted);
		break;
		
	case state_dead_out_of_bounds:
		__DMI->error(error_used_dead_out_of_bounds);
		break;
	}

	// Post error behavior: This cast isn't an error in itself. Do nothing
	// and let the caller deal with it.
	return pointer;
}

// ------------------------------------------------------------------
template <typename T>
template <typename U>
checked_ptr<T>::operator checked_ptr<U>() const
{
	return checked_ptr<U>(pointer);
}

// ------------------------------------------------------------------
template <typename T>
typename checked_ptr<T>::reference_type checked_ptr<T>::operator[](int index)
	const
{
	switch(state())
	{
	case state_alive:
		break;

	case state_null:
		__DMI->error(error_deref_null_index_op);
		break;

	case state_dead_uninitialized:
		__DMI->error(error_deref_uninitialized_index_op);
		break;
		
	case state_dead_deleted:
		__DMI->error(error_deref_deleted_index_op);
		break;
		
	case state_dead_out_of_bounds:
		__DMI->error(error_deref_out_of_bounds_index_op);
		break;
	}

	pointer_type new_pointer = pointer + index;

	mem_info* addr_info = __DMI->address_info(pointer);

	if(addr_info)
	{
		if(!addr_info->is_array)
		{
			__DMI->error(error_index_non_array);
		}
		else
		{
			calculating_bounds_checker<T> checker(addr_info);
			
			if(!checker.contains(new_pointer, false))
			{
				size_t size = addr_info->array_size;
				__DMI->error(error_index_out_of_bounds, index, size - 1);
			}
		}		
	}

	// Post error behavior: There is nothing we can do here, we have to
	// return a valid reference. Just let the unrecoverable error occur.
	return *new_pointer;
}

// ------------------------------------------------------------------
template <typename T>
pointer_state checked_ptr<T>::state() const
{
	// The order of these checks matters -- for example, the out-of-bounds
	// check comes first because it could be confused with the deleted state,
	// since manager::is_checked would return false if the pointer was moved
	// far enough out of its memory block.

	if(out_of_bounds)
	{
		return state_dead_out_of_bounds;
	}
	else if(pointer == reinterpret_cast<pointer_type>(__DMI->uninit_handle()))
	{
		return state_dead_uninitialized;
	}
	else if(pointer == 0)
	{
		return state_null;
	}
	else if(!__DMI->is_checked(pointer, tag))
	{
		return state_dead_deleted;
	}
	else
	{
		return state_alive;
	}
}

// ------------------------------------------------------------------
template <typename T>
void checked_ptr<T>::store_type_info(mem_info* addr_info)
{
	// Initialize supplemental information for the memory block that could
	// not be computed in operator new.

	char type_name[DEREFEREE_MAX_SYMBOL_LEN];
#ifdef _MSC_VER
	strncpy_s(type_name, DEREFEREE_MAX_SYMBOL_LEN, typeid(value_type).name(),
		_TRUNCATE);
#else
	strncpy(type_name, typeid(value_type).name(), DEREFEREE_MAX_SYMBOL_LEN);
#endif
	current_platform()->demangle_type_name(type_name);

	size_t bufsize = strlen(type_name) + 1;
	addr_info->type_name = (char*)malloc(bufsize);
#ifdef _MSC_VER
	strcpy_s(addr_info->type_name, bufsize, type_name);
#else
	strcpy(addr_info->type_name, type_name);
#endif

	addr_info->cookie_size = 0;
	addr_info->array_size = 0;
	addr_info->cookie_size_is_known = false;
}

// ------------------------------------------------------------------
template <typename U, typename V>
void do_relational_check(const checked_ptr<U>& lhs, const checked_ptr<V>& rhs)
{
	pointer_state lhs_state = lhs.state();
	pointer_state rhs_state = rhs.state();

	if(lhs_state == state_dead_out_of_bounds ||
			rhs_state == state_dead_out_of_bounds)
	{
		__DMI->error(error_compare_dead_out_of_bounds);
	}
	else if(lhs_state == state_dead_deleted ||
			rhs_state == state_dead_deleted)
	{
		__DMI->error(error_compare_dead_deleted);
	}
	else if(lhs_state == state_dead_uninitialized ||
			rhs_state == state_dead_uninitialized)
	{
		__DMI->error(error_compare_dead_uninitialized);
	}
	else if((lhs.pointer == NULL && rhs.pointer != NULL) ||
			(lhs.pointer != NULL && rhs.pointer == NULL))
	{
		__DMI->error(error_inequality_one_side_null);
	}
	else
	{
		const mem_info* lhs_info = __DMI->address_info(lhs.pointer);
		const mem_info* rhs_info = __DMI->address_info(rhs.pointer);
		
		if(lhs_info != rhs_info)
		{
			__DMI->error(error_relational_different_blocks);
		}
	}

	// Post error behavior: Do nothing, this is not an unrecoverable error.
}

} // namespace Dereferee

#undef __DMI
