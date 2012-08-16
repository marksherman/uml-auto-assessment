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

#ifndef DEREFEREE_MANAGER_H
#define DEREFEREE_MANAGER_H

#include <cstdio>
#include <memory>
#include <typeinfo>

#include "config.h"
#include "types.h"
#include "option.h"
#include "platform.h"
#include "listener.h"
#include "memtab.h"
#include "usage_stats_impl.h"

#ifndef DEREFEREE_DISABLED

// ===========================================================================

namespace Dereferee
{

// ============================================================================
/**
 * The Dereferee memory manager class is not intended to be used directly
 * by client code. Its methods are only called by the checked_ptr class and
 * related code to manage memory used throughout the execution of a program.
 */
class manager
{
private:
	/**
	 * The singleton instance of the Dereferee memory manager.
	 */
	static manager* _instance;

	/**
	 * Keeps track of the next unique pointer tag.
	 */
	memtag_t _next_tag;

	/**
	 * A unique block of memory allocated to flag checked pointers as
	 * uninitialized. When a checked_ptr object is declared but not assigned a
	 * value, the default constructor will set its pointer value to point to
	 * this address, which can be used to detect accesses to uninitialized
	 * pointers.
	 */	
	void* _uninit_handle;

	/**
	 * A table that keeps track of memory blocks that have moved into a checked
	 * context (that is, once allocated, they have been assigned to a checked
	 * pointer).
	 */
	memtab_entry* _checked_table;

	/**
	 * A table that keeps track of memory blocks that are still unchecked (that
	 * is, have been allocated but have not yet been assigned to a checked
	 * pointer).
	 */
	memtab_entry* _unchecked_table;

	/**
	 * The number of currently allocated blocks of memory that are assigned
	 * to checked pointers.
	 */
	size_t _checked_table_size;

	/**
	 * The number of currently allocated blocks of memory that have not yet
	 * been assigned to checked pointers.
	 */
	size_t _unchecked_table_size;
	
	/**
	 * A pointer to a platform object that is used to acquire platform-
	 * specific information about a memory allocation, such as backtraces.
	 */
	platform* _platform;

	/**
	 * A pointer to a listener object that is used to report the current
	 * memory allocations and errors that occur at runtime.
	 */
	listener* _listener;

	/**
	 * Used to maintain memory usage statistics throughout the execution of
	 * the program.
	 */
	usage_stats_impl _usage_stats;

	// -----------------------------------------------------------------------
	/**
	 * Initializes the memory manager object.
	 */
	manager();
	
	// -----------------------------------------------------------------------
	/**
	 * Returns the next unique tag (up to 2^32 - 1) for memory address
	 * reuse tracking.
	 */
	memtag_t next_tag();

	// -----------------------------------------------------------------------
	/**
	 * Displays the contents of the memory allocation table.  Called when
	 * execution is complete to report memory leaks.
	 */
	void report_leaked_entry(memtab_entry* entry, size_t max_log,
							 size_t& reports_logged);

	// -----------------------------------------------------------------------
	/**
	 * Parses any platform options specified at compile-time (as a
	 * preprocessor definition) or at runtime (as an environment variable)
	 * and creates the platform object.
	 */ 
	void initialize_platform();

	// -----------------------------------------------------------------------
	/**
	 * Parses any listener options specified at compile-time (as a
	 * preprocessor definition) or at runtime (as an environment variable)
	 * and creates the listener object.
	 */ 
	void initialize_listener();

	// -----------------------------------------------------------------------
	/**
	 * A helper function to add an option to the array of options, replacing
	 * an entry that has the same key if it's already in the array.
	 */
	void add_option(option*& options, size_t& num_options,
			size_t& cap_options, const char* key, const char* value);

public:
	// -----------------------------------------------------------------------
	/**
	 * Returns the singleton instance of the Dereferee memory manager,
	 * creating it first if necessary.
	 *
	 * @returns the single Dereferee::manager instance
	 */
	static manager* instance();

	// -----------------------------------------------------------------------
	/**
	 * Releases any resources associated with the memory manager.
	 */
	~manager();

	// -----------------------------------------------------------------------
	/**
	 * Returns a value indicating whether the assigned pointer table contains
	 * the specified address and tag; that is, whether the memory address is
	 * live.
	 * 
	 * @param address the memory address to check
	 * @param tag the unique tag associated with the pointer
	 * @returns true if the address is live; otherwise, false.
	 */
	bool is_checked(const void* address, memtag_t tag);
	
	// -----------------------------------------------------------------------
	/**
	 * Returns the unique pointer that is used to initialize checked pointers
	 * through their default constructor; that is, checked pointers that are
	 * analogous to uninitialized raw pointers.
	 *
	 * @returns the unique pointer used to initialize default checked pointers.
	 */
	void* uninit_handle();
	
	// -----------------------------------------------------------------------
	/**
	 * Increments the reference count of the specified memory address to
	 * indicate that another checked pointer has been created that points to
	 * it.
	 * 
	 * @param address the memory address being referenced
	 */
	void retain(const void* address);
	
	// -----------------------------------------------------------------------
	/**
	 * Decrements the reference count of the specified memory address to
	 * indicate that a checked pointer that points to it has gone out of
	 * scope or been overwritten.
	 * 
	 * @param address the memory address being unreferenced
	 */
	void release(const void* address);
	
	// -----------------------------------------------------------------------
	/**
	 * Returns the number of live references to the specified memory address.
	 * 
	 * @param address the memory address being checked
	 * @returns the number of live references to the address
	 */
	refcount_t ref_count(const void* address);
	
	// -----------------------------------------------------------------------
	/**
	 * Gets descriptive information about the memory block that contains the
	 * specified address, if any.
	 *
	 * @param address the address to search for
	 * @param is_checked a pointer to a boolean variable that will be set to
	 *     true if the memory has been associated with a checked pointer, or
	 *     to false if it is still unchecked. This argument can be NULL, in
	 *     which case this information will not be provided.
	 * @returns a pointer to a mem_info object that contains information about
	 *     the block of memory that contains the specified address, or NULL if
	 *     no allocated block contains it
	 */
	mem_info* address_info(const void* address, bool* is_checked = NULL);

	// -----------------------------------------------------------------------
	/**
	 * Allocates memory and adds it to the unchecked memory table. This method
	 * is called by operator new/new[] to perform the actual allocation and
	 * maintenance.
	 *
	 * @param size the number of bytes to allocate
	 * @param is_array true if called from operator new[]; false if called
	 *     from operator new
	 * @returns a pointer to the memory that was allocated
	 * @throws std::bad_alloc if there was a problem allocating memory
	 */
	void* allocate_memory(size_t size, bool is_array)
		DEREFEREE_THROW_BAD_ALLOC;

	// -----------------------------------------------------------------------
	/**
	 * Frees the memory at the specified address, performing sanity checks as
	 * necessary to verify that the memory was properly used. This method is
	 * called by operator delete/delete[] to perform the actual deallocation
	 * and maintenance.
	 *
	 * @param address the address of the memory to free
	 * @param is_array true if called from operator delete[]; false if called
	 *     from operator delete
	 */
	void free_memory(void* address, bool is_array);

	// -----------------------------------------------------------------------
	/**
	 * Moves a currently unchecked memory address to the checked pointer
	 * table. This method is called by the checked_ptr class the first time
	 * that a raw pointer is assigned to a checked pointer.
	 * 
	 * @param address the memory address to move
	 * @returns the unique tag associated with this address
	 */
	unsigned long move_to_checked(const void* address);
	
	// -----------------------------------------------------------------------
	/**
	 * Removes a memory address from the unchecked pointer table. This is
	 * called by the overloaded delete operators in order to remove any
	 * references to memory addresses that may have been allocated but never
	 * assigned to a checked pointer object.
	 * 
	 * @param address the memory address to remove
	 */	
	void remove_unchecked(const void* address);

	// -----------------------------------------------------------------------
	/**
	 * Removes a memory address from the checked pointer table. This is called
	 * by the checked pointer class when the final reference to a memory
	 * address is released.
	 * 
	 * @param address the memory address to remove
	 */
	void remove_checked(const void* address);

	// -----------------------------------------------------------------------
	/**
	 * Logs a pointer error to the listener.
	 */
	void error(error_code code, ...);

	// -----------------------------------------------------------------------
	/**
	 * Logs a pointer warning to the listener.
	 */
	void warning(warning_code code, ...);

	// -----------------------------------------------------------------------
	/**
	 * Prints a report of any memory that is still currently allocated but
	 * not freed at runtime. 
	 */
	void report_usage();

	// -----------------------------------------------------------------------
	/**
	 * This class contains a custom allocator that prevents its construction
	 * from interfering with the memory tracking logic.
	 */
	void* operator new(size_t size);
	void operator delete(void* ptr);	
};


// ===========================================================================
/**
 * The remove_const template is a traits-like class that permits us to remove
 * the const modifier from a type, if necessary. This is necessary when
 * calculating the cookie size for a particular type because we have to
 * "pretend" to allocate an object of that type, and the type must be non-const
 * so that a compiler error is not generated (because the object being created
 * will not be initialized).
 */
template <typename T>
struct remove_const
{
	typedef T type;
};

template <typename T>
struct remove_const<T const>
{
	typedef T type;
};


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
 * A traits-like class that computes the "cookie" size for a particular type
 * T. When allocating an array of objects whose class has a non-trivial
 * destructor, most compilers (all of those currently tested) increase the size
 * of the memory request by sizeof(size_t) in order to store the number of
 * elements in the array, so that the runtime can iterate over them at the time
 * delete[] is called and execute their destructors.
 *
 * If there were wider support for the TR1 extensions in current C++ compilers,
 * it would be possible to use traits such as has_trivial_destructor and
 * is_pod to determine if a type will cause this cookie to be reserved.
 * Instead, we use the method below, which involves a custom new[] operator to
 * subtract sizeof(T) from the requested size inside the operator, and then
 * throwing an exception to prevent any side effects that might be caused by
 * constructing the object.
 *
 * This method does not pose a problem if the type T does not have a default
 * (parameterless) constructor. In that case, it would not be possible in
 * standard C++ to allocate an array of this type anyway, so the array
 * allocation below does not break any behavioral expectations.
 */

#ifdef _MSC_VER
#pragma warning(push)
#pragma warning(disable:4291)
#endif

 template <typename T>
class cookie_calculator
{
private:
	static size_t _cached_size;

public:
	/**
	 * Returns the size of the cookie that is reserved when allocating an array
	 * of objects of type T. This is usually 0 (for POD types and types with
	 * trivial destructors) or sizeof(size_t) (for types with non-trivial
	 * destructors).
	 *
	 * For performance reasons, this size is only computed once and then
	 * cached for later calls.
	 *
	 * @returns the size of the cookie that is reserved when allocating an
	 *     array of type T
	 */
	static size_t size()
	{
		if(_cached_size == unknown_cookie_size)
		{
			cookie_info info;
			info.type_size = sizeof(T);
			
			// We "pretend" to allocate an array of one object of type T by
			// using a parameterized operator new[] that takes a cookie_info
			// struct as its extra parameter. This allows the operator to
			// subtract off the size of the object itself from the size of the
			// memory block requested by the runtime, and then throw an
			// exception to prevent the object from actually being initialized.

			try { new(info) typename remove_const<T>::type[1]; }
			catch(std::bad_alloc) { }

			_cached_size = info.cookie_size;
		}
		
		return _cached_size;
	}
};

template <typename T>
size_t cookie_calculator<T>::_cached_size = unknown_cookie_size;

#ifdef _MSC_VER
#pragma warning(pop)
#endif

} // namespace Dereferee


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


#endif // DEREFEREE_DISABLED

#endif // DEREFEREE_MANAGER_H
