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

#ifndef DEREFEREE_MEMTAB_H
#define DEREFEREE_MEMTAB_H

#include <cstdlib>
#include <cassert>

#include "types.h"

namespace Dereferee
{

/**
 * The default value of the mem_info::cookie_size field. The cookie size will
 * be set to the proper value the first time a raw pointer to that memory
 * block is assigned to a checked pointer.
 */
const size_t unknown_cookie_size = (size_t)~0;


// ============================================================================
/**
 * Tracks information about a block of memory allocated using the new or new[]
 * operators.
 */
struct mem_info
{
	/**
	 * The address of the memory allocated.
	 */
	const void* address;

	/**
	 * A value from a monotonically increasing set that represents the "time"
	 * at which is block of memory was allocated during execution.
	 */
	memtag_t tag;

	/**
	 * The size of the block of memory that was allocated.
	 */
	size_t block_size;
	
	/**
	 * A reference count indicating the number of checked pointers that
	 * currently point to this block of memory.
	 */
	refcount_t ref_count;
	
	/**
	 * True if this block of memory was allocated with new[]; false if it was
	 * allocated with new.
	 */
	bool is_array;
	
	/**
	 * The implementation-defined string representation of the type that was
	 * specified in the new/new[] expression that allocated this memory, or
	 * NULL if this information is unavailable.
	 * 
	 * It is the responsibility of the listener object to convert this to
	 * something that is human-readable when printing it, usually by
	 * demangling it if it is not already in a human-readable form. 
	 */
	char* type_name;
	
	/**
	 * The size of the "cookie" allocated at the beginning of this memory
	 * block, an implementation-defined space used to store information such as
	 * the number of elements in an array of objects with non-trivial
	 * destructors.
	 */
	size_t cookie_size;
	
	/**
	 * The size of each object in the array if the memory was allocated with
	 * operator new[].
	 */
	size_t element_size;

	/**
	 * A backtrace (represented by an array of addresses where the last entry
	 * is NULL) that indicates the location and context in which this memory
	 * block was allocated.
	 */
	void** backtrace;
	
	// -----------------------------------------------------------------------
	/**
	 * Returns a pointer to the start of the area of this memory block where
	 * actual data is stored (after the "cookie").
	 */
	char* user_begin() const
	{
		assert(cookie_size != unknown_cookie_size);
		return (char*)address + (is_array? cookie_size: 0);
	}
	
	// -----------------------------------------------------------------------
	/**
	 * Returns a pointer to the position in this memory block that marks the
	 * end of actual data storage.
	 */
	char* user_end() const
	{
		return (char*)address + block_size;
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the number of elements in the array represented by this block
	 * of memory.
	 */
	size_t array_size() const
	{
		assert(is_array && cookie_size != unknown_cookie_size);
		return (block_size - cookie_size) / element_size;
	}
};


// ============================================================================
/**
 * This structure represents an entry in the memory table.
 */
struct memtab_entry
{
	memtab_entry *_1, *_2, *_0;
	int _3;
	mem_info info;
};


// ===========================================================================
/*
 *  MEMORY TABLE UTILITY FUNCTIONS
 */

// ---------------------------------------------------------------------------
/**
 * Allocates a new memory table entry and initializes it to default values.
 */
memtab_entry* memtab_alloc();

// ---------------------------------------------------------------------------
/**
 * Releases the memory associated with a memory table entry.
 */
void memtab_free(memtab_entry* entry);

// ---------------------------------------------------------------------------
/**
 * Releases the memory associated with the memory table pointed to by the
 * specified entry.
 */
void memtab_destroy_table(memtab_entry* entry);

// ---------------------------------------------------------------------------
/**
 * Returns the memory table entry associated with the specified memory address.
 */
memtab_entry* memtab_find_address(memtab_entry* entry, const void* address);

// ---------------------------------------------------------------------------
/**
 * Inserts the specified entry into the memory table.
 */
bool memtab_insert_entry(memtab_entry*& entry, memtab_entry* new_entry);

// ---------------------------------------------------------------------------
/**
 * Removes the entry associated with the specified memory address from the
 * memory table.
 */
memtab_entry* memtab_remove_address(memtab_entry*& entry, const void* address);


} // namespace Dereferee

#endif // DEREFEREE_MEMTAB_H
