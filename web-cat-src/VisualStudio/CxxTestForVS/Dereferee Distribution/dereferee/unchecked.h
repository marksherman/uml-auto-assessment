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

#ifndef DEREFEREE_UNCHECKED_H
#define DEREFEREE_UNCHECKED_H


#include <cstdlib>
#include "pointer_traits.h"


namespace Dereferee
{

#ifndef DEREFEREE_NO_DYNAMIC_CAST
template <typename U> class dynamic_cast_helper;
#endif

// ===========================================================================
/**
 * This version of the checked pointer class allows a user to compile out the
 * checks for a release or production build. For most compilers it should not
 * be necessary; if variadic macros are supported, the checked(T*) syntax will
 * resolve directly to T* in those cases. This class is provided as a
 * compromise for environments that do not support this macro.
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

public:
	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that represents an uninitialized pointer.
	 */
	checked_ptr();
	
	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that encapsulates the specified memory
	 * address.
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

	// -----------------------------------------------------------------------
	/**
	 * Returns a value indicating whether the memory addresses of two checked
	 * pointers are equal.
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
	 * pointers are not equal.
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
	 * pointer is less than that of the second.
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
	 * pointer is less than or equal to that of the second.
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
	 * pointer is greater than that of the second.
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
	 * pointer is greater than or equal to that of the second.
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
	 * Dereferences the memory address indicated by this pointer.
	 * 
	 * @returns a reference to the object at the memory address represented
	 *     by this pointer.
	 */
	reference_type operator*();

	// -----------------------------------------------------------------------
	/**
	 * Dereferences the memory address indicated by this pointer in the context
	 * of a struct/class field or method access.
	 * 
	 * @returns the pointer being managed by this checked pointer object.
	 */
	pointer_type operator->();

	// -----------------------------------------------------------------------
	/**
	 * Returns the underlying pointer managed by this checked pointer object.
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
	operator checked_ptr<U>();

	// -----------------------------------------------------------------------
	/**
	 * Accesses an element of the array pointed to by this pointer, if it is
	 * an array pointer.
	 * 
	 * @param index the integer index of the array element to be accessed.
	 * @returns a reference to the element at the specified index in the
	 *     array.
	 */ 
	reference_type operator[](int index);
};

} // namespace Dereferee


/*
 * Include the implementations of the checked_ptr methods. 
 */
#include "unchecked-impl.h"


/*
 * Include the implementations of the enhanced dynamic_cast operator.
 */
#ifndef DEREFEREE_NO_DYNAMIC_CAST
#include "dynamic_cast.h"
#endif


#endif // DEREFEREE_UNCHECKED_H
