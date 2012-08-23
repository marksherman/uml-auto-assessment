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

#ifndef DEREFEREE_PLATFORM_H
#define DEREFEREE_PLATFORM_H

#include <cstdarg>
#include <memory>

#include "option.h"

// ===========================================================================

namespace Dereferee
{

class platform;


// ===========================================================================
/*
 * USING A CUSTOM PLATFORM WITH DEREFEREE
 *
 * If you wish to use a listener other than one of those provided in this
 * package, then you must implement a subclass of Dereferee::listener as well
 * as the following two functions, which are called when the Dereferee manager
 * is created and destroyed, respectively, in order to initialize the listener.
 * This allows clients to customize the notification behavior of Dereferee
 * by linking in a different listener implementation, without requiring any
 * modification to client code using the library.
 *
 * Since the listener class is used internally by the Dereferee memory manager,
 * you should refrain from using the new/delete operators in a custom listener
 * that you write. A listener method can be called in the context of the
 * global new/delete operators, which could cause infinite recursion. This
 * implies that using STL containers is unsafe; instead, prefer managing
 * memory using the C functions malloc(), calloc(), realloc() and free(), if
 * necessary. If you absolutely must use any STL containers, then use custom
 * allocators to ensure that they don't use new/delete.
 *
 * The exception to this rule is when creating the listener itself; since the
 * listener abstract base class overloads the class new/delete operators to
 * bypass Dereferee's tracking versions, you can safely use new/delete to
 * create/destroy the listener.
 */

// ---------------------------------------------------------------------------
/**
 * Creates a new listener object that will be notified of various memory and
 * pointer-related events by the Dereferee memory manager.
 *
 * @param options an array of options to pass to the listener; the last entry
 *     in this array contains NULL in the key and value fields
 * 
 * @returns a pointer to the newly created listener object
 */
extern platform* create_platform(const option* options);

// ---------------------------------------------------------------------------
/**
 * Releases any resources associated with the specified listener.
 *
 * @param listener the listener to be destroyed
 */
extern void destroy_platform(platform* platform);


// ===========================================================================
/**
 * This abstract base class represents the interface used by Dereferee to
 * send notifications about memory usage to a listener object.  Implementors
 * should derive their custom listener from this class if they wish to provide
 * different behavior than the default.
 */
class platform
{
public:
	virtual ~platform() { }

	// -----------------------------------------------------------------------
	/**
	 * This method is called by the Dereferee memory manager whenever it needs
	 * a backtrace of the current function call history -- this mostly occurs
	 * in operator new/new[] so that a backtrace can be saved with each block
	 * of memory that is allocated.
	 *
	 * The backtrace that is returned should be an array of addresses in which
	 * the last entry is NULL. The order of the entries in the backtrace is
	 * most-recent-function first (at index 0) to least-recent-function (e.g.,
	 * main, at index N). If for some reason a backtrace is not able to be
	 * determined, this method should return NULL.
	 * 
	 * It is the responsibility of the listener to try its best to filter
	 * backtraces returned by this method to remove entries corresponding to
	 * internal Dereferee functions and methods -- this is for the user's
	 * benefit. Since every component of Dereferee is wrapped in the Dereferee
	 * namespace, this filtering is easy to do based on the names of the
	 * symbols at each address in the backtrace.
	 *
	 * @param instr_ptr the current instruction pointer from which to start
	 *     the backtrace; use NULL to start from the current location
	 * @param frame_ptr the current frame pointer from which to start the
	 *     backtrace; use NULL to start from the current location
	 *
	 * @returns a pointer to a backtrace, represented as an array of addresses
	 *     where the last entry as NULL, or NULL if the backtrace could not be
	 *     determined
	 */
	virtual void** get_backtrace(void* /* instr_ptr */, void* /* frame_ptr */)
	{ return NULL; }
	
	// -----------------------------------------------------------------------
	/**
	 * This method is used to free the memory that was allocated to store the
	 * backtrace returned by the get_backtrace method.
	 *
	 * This method should gracefully handle NULL as its argument.
	 *
	 * @param backtrace a pointer to a backtrace that was obtained by calling
	 *     get_backtrace
	 */
	virtual void free_backtrace(void** /* backtrace */) { }

	// -----------------------------------------------------------------------
	/**
	 * This method is used to obtain source file information about a frame in
	 * a backtrace. It will typically be called by a listener to obtain human-
	 * readable strings for output.
	 *
	 * @param frame an entry in a backtrace
	 * @param function a buffer that will hold the name of the function
	 *     associated with this frame. This buffer must be large enough to
	 *     hold DEREFEREE_MAX_FUNCTION_LEN characters, including a terminating
	 *     NULL.
	 * @param filename a buffer that will hold the name of the source file
	 *     associated with this frame. This buffer must be large enough to
	 *     hold DEREFEREE_MAX_FILENAME_LEN characters, including a terminating
	 *     NULL.
	 * @param line_number a pointer to an int that will hold the line number
	 *     associated with the frame
	 *
	 * @returns true if successful, false if no information about the frame
	 *     could be obtained
	 */
	virtual bool get_backtrace_frame_info(
		void* /* frame */, char* /* function */,
		char* /* filename */, int* /* line_number */) { return false; }

	// -----------------------------------------------------------------------
	/**
	 * Override the class-specific allocator and deallocator methods so that
	 * the creation of listener subclasses will not interfere with the memory
	 * tracking in the global operators.
	 */
	void* operator new(size_t size) { return malloc(size); }
	void operator delete(void* ptr) { free(ptr); }
};


#define DEREFEREE_MAX_FUNCTION_LEN 512
#define DEREFEREE_MAX_FILENAME_LEN 512

} // namespace Dereferee


#endif // DEREFEREE_PLATFORM_H
