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

#ifndef DEREFEREE_BOUNDS_CHECKER_H
#define DEREFEREE_BOUNDS_CHECKER_H

#include <dereferee/cookie_calculator.h>

namespace Dereferee
{

// ===========================================================================
/**
 * A utility class used to determine if an address is within the bounds of
 * a block of memory, taking into account other characteristics such as the
 * cookie that may be allocated before it.
 *
 * The type parameter T should be the type of element in the memory block,
 * not the pointer type, so this needs to be stripped when passed in if
 * necessary.  (That is, if called from a checked(int*), here T should be
 * int, not int*.)
 *
 * This bounds checker calculates the cookie size upon construction, so it is
 * suitable for use in arithmetic and array indexing operators.
 */
template <typename T>
class calculating_bounds_checker
{
private:
	/**
	 * The memory info block to be bounds-checked.
	 */
	mem_info *addr_info;

public:
	// -----------------------------------------------------------------------
	/**
	 * Initializes a new instance of the bounds checker for the specified
	 * memory block.
	 *
	 * @param info the memory block to be bounds checked
	 */
	explicit calculating_bounds_checker(mem_info *info) : addr_info(info)
	{
		// Compute the cookie size and store it, and the array size, in the
		// mem_info structure.
		
		if (info && !info->cookie_size_is_known)
		{
			info->cookie_size = cookie_calculator<T>::size();
			info->array_size = (info->block_size - info->cookie_size)
				/ sizeof(T);
				
			info->cookie_size_is_known = true;
		}
	}
	
	// -----------------------------------------------------------------------
	/**
	 * Gets a value indicating whether the specified address is contained in
	 * the memory block being bounds checked.
	 *
	 * @param address the address to test for containment
	 * @param inclusive true if the upper bound should be included in the
	 *     containment test (pointers to one-past-the-end can exist), or
	 *     false if the upper bound should not be included (pointers to
	 *     one-past-the-end cannot be dereferenced)
	 *
	 * @returns true if the address is contained in the block; otherwise,
	 *     false.
	 */
	bool contains(const void *address, bool inclusive) const
	{
		// If addr_info is null (meaning the address was invalid, or NULL),
		// returning true means that the caller will not falsely generate
		// an out-of-bounds error; instead, the error will "cascade" until
		// its actual cause is diagnosed.

		if (!addr_info)
			return true;

		const char *p = (const char *) address;
		
		const char *lower_bound = (const char *) addr_info->address
			+ addr_info->cookie_size;
		const char *upper_bound = (const char *) addr_info->address
			+ addr_info->block_size;
		
		if (inclusive)
			return (lower_bound <= p && p <= upper_bound);
		else
			return (lower_bound <= p && p < upper_bound);
	}
};

// ===========================================================================
/**
 * This bounds checker does not calculate the cookie size upon construction,
 * so it is suitable for use in the dereferencing operators * and -> which
 * might be used in non-array contexts.
 */
template <typename T>
class noncalculating_bounds_checker
{
private:
	/**
	 * The memory info block to be bounds-checked.
	 */
	const mem_info *addr_info;

public:
	// -----------------------------------------------------------------------
	/**
	 * Initializes a new instance of the bounds checker for the specified
	 * memory block.
	 *
	 * @param info the memory block to be bounds checked
	 */
	explicit noncalculating_bounds_checker(const mem_info *info) :
		addr_info(info) { }
	
	// -----------------------------------------------------------------------
	/**
	 * Gets a value indicating whether the specified address is contained in
	 * the memory block being bounds checked.
	 *
	 * @param address the address to test for containment
	 * @param inclusive true if the upper bound should be included in the
	 *     containment test (pointers to one-past-the-end can exist), or
	 *     false if the upper bound should not be included (pointers to
	 *     one-past-the-end cannot be dereferenced)
	 *
	 * @returns true if the address is contained in the block; otherwise,
	 *     false.
	 */
	bool contains(const void *address, bool inclusive) const
	{
		// If addr_info is null (meaning the address was invalid, or NULL),
		// returning true means that the caller will not falsely generate
		// an out-of-bounds error; instead, the error will "cascade" until
		// its actual cause is diagnosed.

		if (!addr_info)
			return true;

		const char *p = (const char *) address;
		
		const char *lower_bound = (const char *) addr_info->address
			+ addr_info->cookie_size;
		const char *upper_bound = (const char *) addr_info->address
			+ addr_info->block_size;
		
		if (inclusive)
			return (lower_bound <= p && p <= upper_bound);
		else
			return (lower_bound <= p && p < upper_bound);
	}
};

} // end namespace Dereferee

#endif // DEREFEREE_BOUNDS_CHECKER_H
