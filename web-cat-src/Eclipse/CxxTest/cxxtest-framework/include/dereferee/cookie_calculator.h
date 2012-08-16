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

#ifndef DEREFEREE_COOKIE_CALCULATOR_H
#define DEREFEREE_COOKIE_CALCULATOR_H

#include <cstdlib>

namespace Dereferee
{

// ===========================================================================
/**
 * This structure is passed into the parameterized new[] operator that is used
 * to compute the cookie size for a type.
 */
struct cookie_info
{
	/**
	 * The size of the type whose cookie size is being requested. Passed into
	 * the new[] operator.
	 */
	size_t type_size;
	
	/**
	 * The cookie size for the type being allocated. Passed out of the new[]
	 * operator.
	 */
	size_t cookie_size;
};


// ===========================================================================
/**
 * A lowest-common-denominator implementation of the cookie calculator that
 * determines the size by faux-allocating a one-element array of type T but
 * throwing an exception before construction occurs to prevent side-effects.
 * Until the TR1 type traits extensions in C++0x are properly and
 * consistently implemented in more compilers, this is the safest and most
 * correct way to compute the cookie size.
 *
 * Since this scheme requires the use of a new-expression, this prevents the
 * use of cookie_calculator for types that do not implement a parameterless
 * constructor. This normally would not pose a problem since one cannot
 * allocate an array of such objects anyway, but it forces us to restrict
 * using the cookie calculator in those checked_ptr<> operators that would
 * only be used in an array context (arithmetic and indexing), or else such
 * classes would not be able to be used with any checked pointers, array or
 * not.
 *
 * The above means that the cookie calculation will be done only "on demand",
 * upon the first arithmetic or indexing usage. No errors should slip by due
 * to this, however, because those are the only ways for a pointer to move
 * out of bounds -- dereferencing a live pointer that hasn't been
 * arithmetically modified is known to be safe because it must point to the
 * beginning of its block.
 *
 * This scheme will fail to compile in the special case where you allocate an
 * array of derived objects (with a parameterless constructor) but assign it
 * to a pointer of the base type (without a parameterless constructor), like
 * so:
 *
 *    class Base { public: Base(...) { } };
 *    class Derived : public Base { public: Derived() : Base(...) { } };
 *    checked(Base*) p = new Derived[n];
 *
 * It would also fail in the event that Base is an abstract class. However,
 * both of these situations are ill-advised because they do not provide
 * proper polymorphism and would only work properly in the event that the
 * both Base and Derived were the same size in memory.
 */

// ===========================================================================
/**
 * Traits classes that strip the const-qualifier from a type. This is used by
 * the cookie_calculator because the new-expression in the faux-allocation
 * must be non-const to compile properly, even if the checked pointer type
 * is const.
 */
template <typename T>
struct remove_const
{
	typedef T type;
};

template <typename T>
struct remove_const<const T>
{
	typedef T type;
};

// ===========================================================================
/**
 * The actual implementation of the cookie calculator. We currently keep this
 * separate from the cookie_calculator interface below to that this
 * implementation can be replaced on a per-compiler basis if TR1 support
 * proves to yield correct results in all cases.
 */
template <typename T>
class cookie_calculator_impl
{
private:
	/**
	 * The type T with const modifiers stripped so that a faux-allocation can
	 * compile correctly.
	 */
	typedef typename remove_const<T>::type type;

public:
	// -----------------------------------------------------------------------
	/**
	 * Gets the size of the cookie allocated at the front of arrays of type
	 * T[].
	 *
	 * @returns the size of the cookie
	 */
	static size_t size()
	{
		cookie_info info;
		info.cookie_size = 0;
		info.type_size = sizeof(type);

		// We "pretend" to allocate an array of one object of type T by
		// using a parameterized operator new[] that takes a cookie_info
		// struct as its extra parameter. This allows the operator to
		// subtract off the size of the object itself from the size of the
		// memory block requested by the runtime, and then throw an
		// exception to prevent the object from actually being initialized.

		try { new(info) type[1]; }
		catch(std::bad_alloc) { }

		return info.cookie_size;
	}
};


// ===========================================================================
/**
 * "Public" interface for the cookie calculator, used by the other Dereferee
 * classes. This class delegates to one of the compiler-specific
 * cookie_calculator_impl classes defined above.
 */
template <typename T>
class cookie_calculator
{
private:
	/**
	 * Initially false, this is set to true once the cookie size for arrays of
	 * type T[] is known.
	 */
	static bool _cookie_size_is_known;
	
	/**
	 * The size of the cookie allocated at the front of arrays of type T[].
	 */
	static size_t _cached_cookie_size;

public:
	// -----------------------------------------------------------------------
	/**
	 * Gets the size of the cookie allocated at the front of arrays of type
	 * T[].
	 *
	 * @returns the size of the cookie
	 */
	static size_t size()
	{
		if (!_cookie_size_is_known)
		{
			_cached_cookie_size = cookie_calculator_impl<T>::size();
			_cookie_size_is_known = true;
		}
		
		return _cached_cookie_size;
	}
};

template <typename T>
bool cookie_calculator<T>::_cookie_size_is_known = false;

template <typename T>
size_t cookie_calculator<T>::_cached_cookie_size = 0;

} // end namespace Dereferee


// ===========================================================================
/**
 * This parameterized new[] operator does not actually allocate any memory.
 * The info parameter comes in with the type_size parameter initialized to the
 * size of a single object of the type being allocated. The difference between
 * this value and the size parameter yields the cookie size for that type,
 * which is passed back out through the info struct before the exception is
 * thrown.
 */
void* operator new[](size_t size, Dereferee::cookie_info& info)
	DEREFEREE_THROW_BAD_ALLOC;


#endif // DEREFEREE_COOKIE_CALCULATOR_H
