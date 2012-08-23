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

#ifndef DEREFEREE_CHKPTR_H
#define DEREFEREE_CHKPTR_H

// ===========================================================================
/*
 * IMPORTANT NOTE
 *
 * This header contains a definition for a template class called Ptr<>, which
 * was the name of the checked pointer class in very early versions of
 * Dereferee, before it was officially released (and only used by our
 * students). It is syntactically different in the sense that the type inside
 * Ptr<> does not require an asterisk, but checked() does. For example,
 * Ptr<int> is equivalent to checked(int*) in modern releases of Dereferee.
 *
 * This file is provided solely for the benefit of our students who are
 * using older projects or outdated lab materials and should not be used by
 * anyone who wishes to use modern releases of Dereferee. Anyone still using
 * this is encouraged to migrate their code by the compiler warning provided.
 */

#include <dereferee.h>

#warning Ptr<> is obsolete. You should include <dereferee.h> instead of <chkptr.h> and replace any variables of type "Ptr<T>" with "checked(T*)" (note the asterisk in the updated version).


// ===========================================================================
/**
 * The declaration of the obsolete Ptr<> type.
 */
template <typename T>
class Ptr : public ::Dereferee::checked_ptr<T*>
{
public:
	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that represents an uninitialized pointer.
	 */
	Ptr() { }

	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that aliases an existing checked pointer
	 * (the result of either a direct aliasing or of passing the pointer to
	 * a function). This causes the reference count of the memory address to
	 * be incremented.
	 *
	 * @param rhs The checked pointer being aliased.
	 */
	Ptr(const Ptr<T>& rhs) :
		::Dereferee::checked_ptr<T*>(rhs) { }

	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that aliases an existing checked pointer
	 * (the result of either a direct aliasing or of passing the pointer to
	 * a function). This causes the reference count of the memory address to
	 * be incremented.
	 *
	 * @param rhs The checked pointer being aliased.
	 */
	Ptr(const checked_ptr<T*>& rhs) :
		::Dereferee::checked_ptr<T*>(rhs) { }

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
	Ptr(typename ::Dereferee::pointer_traits<T*>::pointer_type ptr) :
		::Dereferee::checked_ptr<T*>(ptr) { }

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
	Ptr(const ::Dereferee::dynamic_cast_helper<U*>& rhs) :
		::Dereferee::checked_ptr<T*>(rhs) { }

	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that points to the result of the specified
	 * dynamic_cast<U* const> operation. U* const need not be equivalent to T*,
	 * only that U* const is assignable to T*.
	 *
	 * @param rhs the result of a dynamic_cast operation
	 */
	template <typename U>
	Ptr(const ::Dereferee::dynamic_cast_helper<U* const>& rhs) :
		::Dereferee::checked_ptr<T*>(rhs) { }
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
	Ptr(const ::Dereferee::const_cast_helper<U*>& rhs) :
		::Dereferee::checked_ptr<T*>(rhs) { }

	// -----------------------------------------------------------------------
	/**
	 * Creates a checked pointer that points to the result of the specified
	 * const_cast<U* const> operation. U* const need not be equivalent to T*,
	 * only that U* const is assignable to T*.
	 *
	 * @param rhs the result of a const_cast operation
	 */
	template <typename U>
	Ptr(const ::Dereferee::const_cast_helper<U* const>& rhs) :
		::Dereferee::checked_ptr<T*>(rhs) { }
#endif
};

#endif // DEREFEREE_CHKPTR_H
