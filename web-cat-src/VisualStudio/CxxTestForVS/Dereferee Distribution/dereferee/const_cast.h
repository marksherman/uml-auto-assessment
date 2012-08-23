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

#ifndef DEREFEREE_CONST_CAST_H
#define DEREFEREE_CONST_CAST_H

#ifdef _MSC_VER
#pragma warning(push)
#pragma warning(disable:4512)
#endif

// ===========================================================================
/**
 * This file contains template specializations to support using checked
 * pointers with the C++ const_cast operator.
 * 
 * The partial template specializations used to make this work are somewhat
 * complex -- if your compiler has issues with this code, write a detector for
 * it in <dereferee/config.h> and define the symbol DEREFEREE_NO_CONST_CAST,
 * and these definitions will not be included. In this case, you will have to
 * explicitly cast a checked_ptr<T*> back to T* before using it as the operand
 * of a const_cast.
 */

#ifdef const_cast
#undef const_cast
#endif

namespace Dereferee
{


// ===========================================================================
/**
 * The const_cast_helper class provides a wrapper around const_cast that
 * permits checked pointers to be passed into the const_cast operator
 * (without it, the compiler will complain that a checked pointer is not a
 * pointer type because it does not attempt to apply the built-in operator T*
 * conversion). The template parameter U, as with a standard const cast,
 * corresponds to the destination type of the cast.
 *
 * The unspecialized version of const_cast_helper does not provide any
 * functionality because specializations are present for all of the valid
 * pointer and reference types.
 */
template <typename U>
class const_cast_helper { };


/**
 * The specialization for U* permits a raw pointer T* or a checked pointer
 * checked_ptr<T*> to be cast to a raw pointer U*. Furthermore, checked_ptr<U*>
 * has a constructor and assignment operator that take a
 * const_cast_helper<U*> as its argument, so that a checked pointer can be
 * directly initialized from a const_cast result (without this, the
 * assignment would fail because the compiler won't apply the user-defined
 * conversion const_cast_helper<U*> --> U* --> checked_ptr<U*>).
 */
template <typename U>
class const_cast_helper<U*>
{
private:
	/**
	 * Stores a pointer after it has been cast in the initializer.
	 */
	U* cast_value;
	
public:
	// -----------------------------------------------------------------------
	/**
	 * Dynamic casts the specified T* pointer to U*, storing the result.
	 *
	 * @param value_to_cast the pointer to cast to U*
	 */
	template <typename T>
	const_cast_helper(T* value_to_cast) :
		cast_value(const_cast<U*>(value_to_cast))
	{
	}

	// -----------------------------------------------------------------------
	/**
	 * Dynamic casts the pointer wrapped by the specified checked_ptr<T*>
	 * pointer to U*, storing the result.
	 *
	 * @param value_to_cast the pointer to cast to U*
	 */
	template <typename T>
	const_cast_helper(const checked_ptr<T*>& value_to_cast) :
		cast_value(const_cast<U*>((T*)value_to_cast))
	{
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the result of the const_cast that occurred when this helper
	 * object was initialized.
	 *
	 * @returns the result of the const_cast.
	 */
	operator U*() const
	{
		return cast_value;
	}
};


/**
 * The specialization permits a raw pointer T* const or a checked pointer
 * checked_ptr<T* const> to be cast to a raw pointer U* const.
 *
 * This syntax seems unnecessary since a const_cast<U*> could just as easily
 * be assigned to a pointer of type U* const, but C++ permits it, so we mimic
 * it.
 */
template <typename U>
class const_cast_helper<U* const>
{
private:
	/**
	 * Stores a pointer after it has been cast in the initializer.
	 */
	U* const cast_value;
	
public:
	// -----------------------------------------------------------------------
	/**
	 * Dynamic casts the specified T* pointer to U*, storing the result.
	 *
	 * @param value_to_cast the pointer to cast to U*
	 */
	template <typename T>
	const_cast_helper(T* value_to_cast) :
		cast_value(const_cast<U* const>(value_to_cast))
	{
	}

	// -----------------------------------------------------------------------
	/**
	 * Dynamic casts the pointer wrapped by the specified checked_ptr<T*>
	 * pointer to U*, storing the result.
	 *
	 * @param value_to_cast the pointer to cast to U*
	 */
	template <typename T>
	const_cast_helper(const checked_ptr<T*>& value_to_cast) :
		cast_value(const_cast<U* const>((T*)value_to_cast))
	{
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the result of the const_cast that occurred when this helper
	 * object was initialized.
	 *
	 * @returns the result of the const_cast.
	 */
	operator U* const() const
	{
		return cast_value;
	}
};


/**
 * The specialization for U& permits a non-const reference T& to be cast to a
 * non-const reference U&.
 */
template <typename U>
class const_cast_helper<U&>
{
private:
	/**
	 * Stores the reference after it has been cast in the initializer.
	 */
	U& cast_value;
	
public:
	// -----------------------------------------------------------------------
	/**
	 * Dynamic casts the specified T& reference to U&, storing the result.
	 *
	 * @param value_to_cast the non-const reference to cast to U&
	 */
	template <typename T>
	const_cast_helper(T& value_to_cast) :
		cast_value(const_cast<U&>(value_to_cast))
	{
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the result of the const_cast that occurred when this helper
	 * object was initialized.
	 *
	 * @returns the result of the const_cast.
	 */
	operator U&() const
	{
		return cast_value;
	}
};


/**
 * The specialization for U& permits a const or non-const reference T& to be
 * cast to a const reference U&.
 */
template <typename U>
class const_cast_helper<const U&>
{
private:
	/**
	 * Stores the reference after it has been cast in the initializer.
	 */
	const U& cast_value;
	
public:
	// -----------------------------------------------------------------------
	/**
	 * Dynamic casts the specified T& reference to U&, storing the result.
	 *
	 * @param value_to_cast the non-const reference to cast to U&
	 */
	template <typename T>
	const_cast_helper(const T& value_to_cast) :
		cast_value(const_cast<const U&>(value_to_cast))
	{
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the result of the const_cast that occurred when this helper
	 * object was initialized.
	 *
	 * @returns the result of the const_cast.
	 */
	operator const U&() const
	{
		return cast_value;
	}
};

} // namespace Dereferee


// ===========================================================================
/**
 * This macro overrides the definition of const_cast to ensure that our
 * specialized version is called. This should not break any other syntactic
 * constructs, since as far as we know, const_cast is only valid in those
 * contexts where the constructor call above would also be accepted.
 */
#define const_cast ::Dereferee::const_cast_helper


#ifdef _MSC_VER
#pragma warning(pop)
#endif

#endif // DEREFEREE_CONST_CAST_H
