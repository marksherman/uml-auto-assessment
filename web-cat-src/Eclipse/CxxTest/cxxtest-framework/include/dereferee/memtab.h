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

#include <dereferee/types.h>

namespace Dereferee
{

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
	 * The size of the cookie allocated before this block, if this block is an
	 * array of objects with non-trivial destructors.
	 */
	size_t cookie_size;

	/**
	 * The size of the array that this block represents, if it is an array.
	 * This is filled at the same time that the cookie size is populated.
	 */
	size_t array_size;

	/**
	 * Initially false, this is set to true once the cookie size (and
	 * consequently, the array size) has been determined.
	 */
	bool cookie_size_is_known;

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
	 * A backtrace (represented by an array of addresses where the last entry
	 * is NULL) that indicates the location and context in which this memory
	 * block was allocated.
	 */
	void** backtrace;
	
	/**
	 * A listener-specific value that is associated with the memory block.
	 */
    void* user_info;
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
