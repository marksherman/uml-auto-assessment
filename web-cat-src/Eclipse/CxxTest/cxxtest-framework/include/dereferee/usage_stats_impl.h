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

#ifndef DEREFEREE_USAGE_STATS_IMPL_H
#define DEREFEREE_USAGE_STATS_IMPL_H

#include <cstdarg>
#include <dereferee/listener.h>

namespace Dereferee
{

// ===========================================================================
/**
 * A concrete implementation of the Dereferee::usage_stats interface, used by
 * the memory manager to report memory usage statistics to the listener. This
 * class is not intended to be used by clients.
 */
class usage_stats_impl : public usage_stats
{
private:
	/**
	 * The number of memory leaks that occurred.
	 */
	unsigned int _leaks;
	
	/**
	 * The total number of bytes allocated throughout the execution of the
	 * program.
	 */
	size_t _total_bytes_allocated;
	
	/**
	 * The number of bytes currently allocated to the program.
	 */
	size_t _current_bytes_allocated;
	
	/**
	 * The largest number of bytes that were allocated at any one point
	 * during the execution of the program.
	 */
	size_t _maximum_bytes_in_use;
	
	/**
	 * The number of calls made to the non-array new operator.
	 */
	size_t _calls_to_new;

	/**
	 * The number of calls made to the non-array delete operator with a
	 * non-null argument.
	 */
	size_t _calls_to_delete;

	/**
	 * The number of calls made to the array new[] operator.
	 */
	size_t _calls_to_array_new;

	/**
	 * The number of calls made to the array delete[] operator with a
	 * non-null argument.
	 */
	size_t _calls_to_array_delete;

	/**
	 * The number of calls made to the non-array delete operator with a
	 * null argument.
	 */
	size_t _calls_to_delete_null;

	/**
	 * The number of calls made to the non-array delete[] operator with a
	 * null argument.
	 */
	size_t _calls_to_array_delete_null;
	
public:
	// -----------------------------------------------------------------------
	/**
	 * Initializes a new usage_stats_impl object.
	 */
	usage_stats_impl();
	
	// -----------------------------------------------------------------------
	size_t leaks() const;

	// -----------------------------------------------------------------------
	size_t total_bytes_allocated() const;

	// -----------------------------------------------------------------------
	size_t maximum_bytes_in_use() const;

	// -----------------------------------------------------------------------
	size_t calls_to_new() const;

	// -----------------------------------------------------------------------
	size_t calls_to_array_new() const;

	// -----------------------------------------------------------------------
	size_t calls_to_delete() const;

	// -----------------------------------------------------------------------
	size_t calls_to_array_delete() const;

	// -----------------------------------------------------------------------
	size_t calls_to_delete_null() const;

	// -----------------------------------------------------------------------
	size_t calls_to_array_delete_null() const;
	
	// -----------------------------------------------------------------------
	/**
	 * Sets the number of leaks that were recorded during execution.
	 *
	 * @param leaks the number of memory leaks
	 */
	void set_leaks(size_t leaks);

	// -----------------------------------------------------------------------
	/**
	 * Updates the usage statistics based on an allocation made by the user.
	 *
	 * @param size the number of bytes allocated
	 * @param is_array true if the allocation was made with new[]; otherwise,
	 *     false
	 */
	void record_allocation(size_t size, bool is_array);

	// -----------------------------------------------------------------------
	/**
	 * Updates the usage statistics based on a deallocation made by the user.
	 *
	 * @param size the number of bytes allocated
	 * @param is_array true if the allocation was made with new[]; otherwise,
	 *     false
	 */
	void record_deallocation(size_t size, bool is_array);

	// -----------------------------------------------------------------------
	/**
	 * Updates the usage statistics based on an attempt to delete a null
	 * pointer (a valid operation).
	 */
	void record_null_deallocation(bool is_array);
};

} // namespace Dereferee

#endif // DEREFEREE_USAGE_STATS_IMPL_H
