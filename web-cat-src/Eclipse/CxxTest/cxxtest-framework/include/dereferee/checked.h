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

#ifndef DEREFEREE_CHECKED_H
#define DEREFEREE_CHECKED_H

#include <cstdio>
#include <memory>
#include <typeinfo>
#include <dereferee/types.h>
#include <dereferee/manager.h>
#include <dereferee/pointer_traits.h>

namespace Dereferee
{

#ifndef DEREFEREE_NO_DYNAMIC_CAST
template <typename U> class dynamic_cast_helper;
#endif

#ifndef DEREFEREE_NO_CONST_CAST
template <typename U> class const_cast_helper;
#endif


// ===========================================================================
/**
 * Possible states that a pointer can be in.
 */
enum pointer_state
{
	/* The pointer is alive -- it points to memory that is currently
	   allocated and has never been moved outside of that block during its
	   life span. */
	state_alive = 0,
	
	/* The pointer is null. */
	state_null,
	
	/* The pointer has been declared but has not yet been assigned a
	   value. */
	state_dead_uninitialized,
	
	/* The pointer used to point to allocated memory, but this memory has
	   since been deleted. */
	state_dead_deleted,
	
	/* The pointer used to point to allocated memory, but pointer
	   arithmetic has moved it out of bounds. */
	state_dead_out_of_bounds
};


// ===========================================================================
/**
 * checked_ptr is a checked pointer class that should be used in lieu of T* in
 * C++ code. The checked pointer does not automatically manage memory as a
 * smart pointer does; rather, its operations are tracked and errors in usage
 * that would manifest themselves as crashes or unpredictable behavior at
 * runtime are instead reported to a listener object that is linked into the
 * executable (see listener.h for details).
 *
 * For any pointer type T*, checked_ptr<T*> can be used as a drop-in
 * replacement. Only the declaration of the pointer variable needs to be
 * changed; no other syntactic modifications have to be made in source code.
 */
template <typename T>
class checked_ptr
{
private:
	/* These are absorbed into the checked_ptr class for brevity elsewhere. */
	typedef typename pointer_traits<T>::value_type value_type;
	typedef typename pointer_traits<T>::pointer_type pointer_type;
	typedef typename pointer_traits<T>::const_pointer_type const_pointer_type;
	typedef typename pointer_traits<T>::reference_type reference_type;
	typedef typename pointer_traits<T>::const_reference_type
		const_reference_type;

	// -----------------------------------------------------------------------
	/**
	 * The actual memory address referenced by this pointer.
	 */
	pointer_type pointer;

	// -----------------------------------------------------------------------
	/**
	 * A unique (up to 2^32 - 1) tag that indicates "when" the memory was
	 * allocated. Since memory addresses can be reused after a deallocation/
	 * allocation cycle, the tag provides a way to identify separate uses
	 * of the same address.
	 */
	memtag_t tag;
	
	// -----------------------------------------------------------------------
	/**
	 * Indicates whether pointer arithmetic has moved this pointer out of
	 * bounds. This flag is initialized to false, and once it becomes true,
	 * it will permanently remain true until the pointer itself goes out of
	 * scope. These semantics agree with those described in the C++ standard.
	 */
	bool out_of_bounds;

	// -----------------------------------------------------------------------
	/**
	 * Returns an enumeration value that describes the state of this pointer;
	 * i.e., whether it is alive or dead, and if it is dead, the reason for
	 * its death.
	 *
	 * @returns a value from the pointer_state enumeration.
	 */
	pointer_state state() const;

	// -----------------------------------------------------------------------
	/**
	 * Stores supplemental type information about the data in the memory block
	 * represented by this pointer, such as the C++ type name of the type
	 * that was allocated, the type's size, and the size of the cookie
	 * associated with that type.
	 */
	void store_type_info(mem_info* addr_info);

	// -----------------------------------------------------------------------
	/**
	 * Performs a set of sanity checks used by the relational operators:
	 * <, <=, >, and >=.
	 */
	template <typename U, typename V>
	friend void do_relational_check(const checked_ptr<U>& lhs,
									const checked_ptr<V>& rhs);

public:
	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that represents an uninitialized pointer.
	 */
	checked_ptr();
	
	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that aliases an existing checked pointer
	 * (the result of either a direct aliasing or of passing the pointer to
	 * a function). This causes the reference count of the memory address to
	 * be incremented.
	 * 
	 * @param rhs The checked pointer being aliased.
	 */
	checked_ptr(const checked_ptr<T>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that encapsulates the specified memory
	 * address. This is typically only used on the left-hand side of a call
	 * to new, in order to complete the assignment of the newly allocated
	 * memory to the checked pointer.
	 * 
	 * @param ptr A pointer to an object of type T, to be assigned to this
	 *     checked pointer object.
	 */
	checked_ptr(pointer_type ptr);

#ifndef DEREFEREE_NO_DYNAMIC_CAST
	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that points to the result of the specified
	 * dynamic_cast<U*> operation. U* need not be equivalent to T*, only that
	 * U* is assignable to T*.
	 *
	 * @param rhs the result of a dynamic_cast operation 
	 */
	template <typename U>
	checked_ptr(const dynamic_cast_helper<U*>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that points to the result of the specified
	 * dynamic_cast<U* const> operation. U* const need not be equivalent to T*,
	 * only that U* const is assignable to T*.
	 *
	 * @param rhs the result of a dynamic_cast operation
	 */
	template <typename U>
	checked_ptr(const dynamic_cast_helper<U* const>& rhs);
#endif

#ifndef DEREFEREE_NO_CONST_CAST
	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that points to the result of the specified
	 * const_cast<U*> operation. U* need not be equivalent to T*, only that
	 * U* is assignable to T*.
	 *
	 * @param rhs the result of a const_cast operation 
	 */
	template <typename U>
	checked_ptr(const const_cast_helper<U*>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that points to the result of the specified
	 * const_cast<U* const> operation. U* const need not be equivalent to T*,
	 * only that U* const is assignable to T*.
	 *
	 * @param rhs the result of a const_cast operation
	 */
	template <typename U>
	checked_ptr(const const_cast_helper<U* const>& rhs);
#endif

	// -----------------------------------------------------------------------
	/**
	 * Destroys the checked pointer object. Since this is called when a
	 * pointer goes out of scope, if this is the last remaining reference to
	 * the memory address identified by this pointer, an error is generated
	 * that a leak will occur.
	 */
	~checked_ptr();

	// -----------------------------------------------------------------------
	/**
	 * Sets the current checked pointer to alias an existing checked pointer.
	 * This causes the reference count of the memory address to be
	 * incremented. If the current pointer is not dead, a memory leak warning
	 * will be generated if it is the last reference to a particular memory
	 * block.
	 * 
	 * @param rhs The checked pointer being aliased.
	 * @returns a reference to this pointer
	 */
	checked_ptr<T>& operator=(const checked_ptr<T>& rhs);

#ifndef DEREFEREE_NO_DYNAMIC_CAST
	// -----------------------------------------------------------------------
	/**
	 * Assigns the result of the specified dynamic_cast<U*> operation to this
	 * checked pointer. U* need not be equivalent to T*, only that U* is
	 * assignable to T*.
	 *
	 * @param rhs the result of a dynamic_cast operation
	 * @returns a reference to this pointer
	 */
	template <typename U>
	checked_ptr<T>& operator=(const dynamic_cast_helper<U*>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Assigns the result of the specified dynamic_cast<U* const> operation to
	 * this checked pointer. U* const need not be equivalent to T*, only that
	 * U* const is assignable to T*.
	 *
	 * @param rhs the result of a dynamic_cast operation
	 * @returns a reference to this pointer
	 */
	template <typename U>
	checked_ptr<T>& operator=(const dynamic_cast_helper<U* const>& rhs);
#endif

#ifndef DEREFEREE_NO_CONST_CAST
	// -----------------------------------------------------------------------
	/**
	 * Assigns the result of the specified const_cast<U*> operation to this
	 * checked pointer. U* need not be equivalent to T*, only that U* is
	 * assignable to T*.
	 *
	 * @param rhs the result of a const_cast operation
	 * @returns a reference to this pointer
	 */
	template <typename U>
	checked_ptr<T>& operator=(const const_cast_helper<U*>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Assigns the result of the specified const_cast<U* const> operation to
	 * this checked pointer. U* const need not be equivalent to T*, only that
	 * U* const is assignable to T*.
	 *
	 * @param rhs the result of a const_cast operation
	 * @returns a reference to this pointer
	 */
	template <typename U>
	checked_ptr<T>& operator=(const const_cast_helper<U* const>& rhs);
#endif

	// -----------------------------------------------------------------------
	/**
	 * Returns a value indicating whether the memory addresses of two checked
	 * pointers are equal. If either pointer is dead, an error is generated.
	 * 
	 * @param lhs the first pointer to compare
	 * @param rhs the second pointer to compare
	 * @returns true if the pointers point to the same address; otherwise,
	 *     false.
	 */
	template <typename U, typename V>
	friend bool operator==(const checked_ptr<U>& lhs,
						   const checked_ptr<V>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Returns a value indicating whether the memory addresses of two checked
	 * pointers are not equal. If either pointer is dead, an error is
	 * generated.
	 * 
	 * @param lhs the first pointer to compare
	 * @param rhs the second pointer to compare
	 * @returns true if the pointers point to the same address; otherwise,
	 *     false.
	 */
	template <typename U, typename V>
	friend bool operator!=(const checked_ptr<U>& lhs,
						   const checked_ptr<V>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Returns a value indicating whether the memory address of the first
	 * pointer is less than that of the second. An error is generated if either
	 * pointer is dead, if only one of them is NULL, or if the pointers do not
	 * point into the same allocated block.
	 * 
	 * @param lhs the first pointer to compare
	 * @param rhs the second pointer to compare
	 * @returns true if the pointers point to the same address; otherwise,
	 *     false.
	 */
	template <typename U, typename V>
	friend bool operator<(const checked_ptr<U>& lhs,
						  const checked_ptr<V>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Returns a value indicating whether the memory address of the first
	 * pointer is less than or equal to that of the second. An error is
	 * generated if either pointer is dead, if only one of them is NULL, or if
	 * the pointers do not point into the same allocated block.
	 * 
	 * @param lhs the first pointer to compare
	 * @param rhs the second pointer to compare
	 * @returns true if the pointers point to the same address; otherwise,
	 *     false.
	 */
	template <typename U, typename V>
	friend bool operator<=(const checked_ptr<U>& lhs,
						   const checked_ptr<V>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Returns a value indicating whether the memory address of the first
	 * pointer is greater than that of the second. An error is generated if
	 * either pointer is dead, if only one of them is NULL, or if the pointers
	 * do not point into the same allocated block.
	 * 
	 * @param lhs the first pointer to compare
	 * @param rhs the second pointer to compare
	 * @returns true if the pointers point to the same address; otherwise,
	 *     false.
	 */
	template <typename U, typename V>
	friend bool operator>(const checked_ptr<U>& lhs,
						  const checked_ptr<V>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Returns a value indicating whether the memory address of the first
	 * pointer is greater than or equal to that of the second. An error is
	 * generated if either pointer is dead, if only one of them is NULL, or if
	 * the pointers do not point into the same allocated block.
	 * 
	 * @param lhs the first pointer to compare
	 * @param rhs the second pointer to compare
	 * @returns true if the pointers point to the same address; otherwise,
	 *     false.
	 */
	template <typename U, typename V>
	friend bool operator>=(const checked_ptr<U>& lhs,
						   const checked_ptr<V>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Increments the pointer by one element.
	 *
	 * @returns this pointer
	 */
	checked_ptr<T>& operator++();

	// -----------------------------------------------------------------------
	/**
	 * Increments the pointer by one element, returning the original value.
	 *
	 * @returns a pointer that points to the original value before this
	 *     pointer was incremented
	 */
	checked_ptr<T> operator++(int);

	// -----------------------------------------------------------------------
	/**
	 * Decrements the pointer by one element.
	 *
	 * @returns this pointer
	 */
	checked_ptr<T>& operator--();
	
	// -----------------------------------------------------------------------
	/**
	 * Decrements the pointer by one element, returning the original value.
	 *
	 * @returns a pointer that points to the original value before this
	 *     pointer was decremented
	 */
	checked_ptr<T> operator--(int);

	// -----------------------------------------------------------------------
	/**
	 * Returns a pointer that is advanced delta elements forward from this
	 * pointer.
	 *
	 * @param ptr the pointer to advance from
	 * @param delta the number of elements to advance
	 * @returns a pointer that points to memory delta elements after ptr
	 */
	template <typename U>
	friend checked_ptr<U> operator+(const checked_ptr<U>& ptr,
									ptrdiff_t delta);

	// -----------------------------------------------------------------------
	/**
	 * Returns a pointer that is advanced delta elements forward from this
	 * pointer.
	 *
	 * @param delta the number of elements to advance
	 * @param ptr the pointer to advance from
	 * @returns a pointer that points to memory delta elements after ptr
	 */
	template <typename U>
	friend checked_ptr<U> operator+(ptrdiff_t delta,
									const checked_ptr<U>& ptr);

	// -----------------------------------------------------------------------
	/**
	 * Returns a pointer that is moved back delta elements from this pointer.
	 *
	 * @param ptr the pointer to move back from
	 * @param delta the number of elements to move back
	 * @returns a pointer that points to memory delta elements before ptr
	 */
	template <typename U>
	friend checked_ptr<U> operator-(const checked_ptr<U>& ptr,
									ptrdiff_t delta);
	
	// -----------------------------------------------------------------------
	/**
	 * Returns the distance between two pointers.
	 *
	 * @param lhs the pointer to be subtracted from
	 * @param rhs the pointer to be subtracted
	 * @returns a ptrdiff_t value that indicates the distance between the two
	 *     pointers
	 */
	template <typename U, typename V>
	friend ptrdiff_t operator-(const checked_ptr<U>& lhs,
							   const checked_ptr<V>& rhs);

	// -----------------------------------------------------------------------
	/**
	 * Advances this pointer delta elements forward.
	 *
	 * @param delta the number of elements to advance
	 * @returns this pointer
	 */
	checked_ptr<T>& operator+=(ptrdiff_t delta);

	// -----------------------------------------------------------------------
	/**
	 * Moves this pointer back delta elements.
	 *
	 * @param delta the number of elements to move back
	 * @returns this pointer
	 */
	checked_ptr<T>& operator-=(ptrdiff_t delta);

	// -----------------------------------------------------------------------
	/**
	 * Dereferences the memory address indicated by this pointer. An error is
	 * reported if the pointer is dead.
	 * 
	 * @returns a reference to the object at the memory address represented
	 *     by this pointer.
	 */
	reference_type operator*();

	// -----------------------------------------------------------------------
	/**
	 * Dereferences the memory address indicated by this pointer in the context
	 * of a struct/class field or method access. An error is reported if the
	 * pointer is dead.
	 * 
	 * @returns the pointer being managed by this checked pointer object.
	 */
	pointer_type operator->() const;

	// -----------------------------------------------------------------------
	/**
	 * Returns the underlying pointer managed by this checked pointer object.
	 * This permits operations that would be very difficult to implement
	 * without it (the C++ casts and delete, for instance). This also allows
	 * the pointer to be used in contexts where a regular C++ pointer is
	 * required, but it should be used at one's own risk since it bypasses the
	 * enhanced error reporting provided by the checked pointer class.
	 * 
	 * @returns the pointer being managed by this checked pointer object.
	 */
	operator pointer_type() const;

	// -----------------------------------------------------------------------
	/**
	 * Permits a checked pointer of one type to be cast to a checked pointer of
	 * another type. Essentially, this allows checked(T*) to be cast to
	 * checked(U*) only if the compiler will allow T* to be cast to U*.
	 * 
	 * @returns a checked pointer of type checked(U*) that points to the same
	 *     address as this pointer.
	 */
	template <typename U>
	operator checked_ptr<U>() const;

	// -----------------------------------------------------------------------
	/**
	 * Accesses an element of the array pointed to by this pointer, if it is
	 * an array pointer.
	 * 
	 * @param index the integer index of the array element to be accessed.
	 * @returns a reference to the element at the specified index in the
	 *     array.
	 */ 
	reference_type operator[](int index) const;
};

} // namespace Dereferee


/*
 * Include the implementations of the enhanced dynamic_cast operator.
 */
#ifndef DEREFEREE_NO_DYNAMIC_CAST
#include <dereferee/dynamic_cast.h>
#endif


/*
 * Include the implementations of the enhanced const_cast operator.
 */
#ifndef DEREFEREE_NO_CONST_CAST
#include <dereferee/const_cast.h>
#endif


/*
 * Include the implementations of the checked_ptr methods. 
 */
#include <dereferee/checked-impl.h>


#endif // DEREFEREE_CHECKED_H
