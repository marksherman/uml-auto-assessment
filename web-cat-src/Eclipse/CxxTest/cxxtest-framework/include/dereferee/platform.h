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

#include <dereferee/option.h>

// ===========================================================================

namespace Dereferee
{

class platform;


// ===========================================================================
/*
 * USING A CUSTOM PLATFORM WITH DEREFEREE
 *
 * If you wish to use a platform other than one of those provided in this
 * package, then you must implement a subclass of Dereferee::platform as well
 * as the following two functions, which are called when the Dereferee manager
 * is created and destroyed, respectively, in order to initialize the platform.
 * This allows clients to customize the notification behavior of Dereferee
 * by linking in a different platform implementation, without requiring any
 * modification to client code using the library.
 *
 * Since the platform class is used internally by the Dereferee memory manager,
 * you should refrain from using the new/delete operators in a custom platform
 * that you write. A platform method can be called in the context of the
 * global new/delete operators, which could cause infinite recursion. This
 * implies that using STL containers is unsafe; instead, prefer managing
 * memory using the C functions malloc(), calloc(), realloc() and free(), if
 * necessary. If you absolutely must use any STL containers, then use custom
 * allocators to ensure that they don't use new/delete.
 *
 * The exception to this rule is when creating the platform itself; since the
 * platform abstract base class overloads the class new/delete operators to
 * bypass Dereferee's tracking versions, you can safely use new/delete to
 * create/destroy the platform.
 */

// ---------------------------------------------------------------------------
/**
 * Creates a new platform object that will be notified of various memory and
 * pointer-related events by the Dereferee memory manager.
 *
 * @param options an array of options to pass to the platform; the last entry
 *     in this array contains NULL in the key and value fields
 * 
 * @returns a pointer to the newly created platform object
 */
extern platform* create_platform(const option* options);

// ---------------------------------------------------------------------------
/**
 * Releases any resources associated with the specified platform.
 *
 * @param platform the platform to be destroyed
 */
extern void destroy_platform(platform* platform);


// ===========================================================================
/**
 * This abstract base class represents the interface used by Dereferee to
 * request platform-specific information, such as backtraces.  Implementors
 * should derive their custom platform from this class to support compilers
 * and operating systems not already supported by the provided platform
 * modules.
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
	 * It is the responsibility of the platform module to try its best to
	 * filter backtraces returned by this method to remove entries
	 * corresponding to internal Dereferee functions and methods -- this is
	 * for the user's benefit. Since every component of Dereferee is wrapped
	 * in the Dereferee namespace, this filtering is easy to do based on the
	 * names of the symbols at each address in the backtrace.
	 *
	 * This method accepts an instruction pointer and frame pointer as "hints"
	 * for indicating where to begin the backtrace. They can be safely ignored
	 * by platforms for which it would be useful to use this information, and
	 * indeed, Dereferee's internal components always pass NULL for these
	 * arguments. The gcc_*_platform modules do not use these values, but the
	 * msvc_win32_platform does use them when they are passed in by methods in
	 * the corresponding msvc_debugger_Listener module.
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
	 * This method should gracefully handle NULL as its argument, in which it
	 * should do nothing.
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
	 *     hold DEREFEREE_MAX_SYMBOL_LEN characters, including a terminating
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
	 * Converts a mangled C++ type name to a human-readable name. This method
	 * is called _only_ in the context of retrieving the name of a checked
	 * pointer's type, using the RTTI construct typeid(T).name().
	 *
	 * Some compilers, such as GCC, return this as a mangled name, so this
	 * method should be implemented to demangle them on those platforms. Other
	 * compilers, such as Microsoft Visual C++, return a human-readable name
	 * directly, so the default (empty) implementation of this method can be
	 * used.
	 *
	 * @param symbol on input, the name of the symbol to be demangled. On
	 *     output, this should contain the demangled version of the symbol.
	 *     If the symbol is not mangled or could not be demangled, the buffer
	 *     contents should be left unaltered. The buffer must be large enough
	 *     to hold DEREFEREE_MAX_SYMBOL_LEN characters, including a
	 *     terminating NULL.
	 */
	virtual void demangle_type_name(char* /* type_name */) { }

	// -----------------------------------------------------------------------
	/**
	 * Saves the current execution context so that it can be restored later.
	 * Dereferee does not use this method directly, but it is useful for
	 * other libraries that want to integrate with Dereferee and implement
	 * crash recovery (such as unit testing frameworks; CxxTest uses this
	 * method to make sure that backtraces are properly rolled back when
	 * a POSIX signal is handled).
	 *
	 * Currently, the pair of context methods is intended for use by
	 * platforms that keep track of backtraces as functions are called (such
	 * as the gcc_*_platforms, currently), as opposed to those that compute
	 * it on demand (msvc_win32_platform). In the latter case, this method
	 * can be left unimplemented.
	 *
	 * Calls to this method can be nested, so implementors should use a stack
	 * structure to store these contexts.
	 */
	virtual void save_current_context() { }
	
	// -----------------------------------------------------------------------
	/**
	 * Restores the most recent execution context that was saved.
	 *
	 * See the description of the save_current_context method for information
	 * on how to implement this.
	 */
	virtual void restore_current_context() { }

	// -----------------------------------------------------------------------
	/**
	 * Override the class-specific allocator and deallocator methods so that
	 * the creation of listener subclasses will not interfere with the memory
	 * tracking in the global operators.
	 */
	void* operator new(size_t size) { return malloc(size); }
	void operator delete(void* ptr) { free(ptr); }
};


/**
 * The maximum length of the buffer used to hold a symbol name stored by the
 * platform::get_backtrace_frame_info and platform::demangle_symbol methods.
 */
#define DEREFEREE_MAX_SYMBOL_LEN 512

/** Old name for the above symbol. */
#define DEREFEREE_MAX_FUNCTION_LEN DEREFEREE_MAX_SYMBOL_LEN

/**
 * The maximum length of the buffer used to hold a source file name stored by
 * the platform::get_backtrace_frame_info method.
 */
#define DEREFEREE_MAX_FILENAME_LEN 512


} // namespace Dereferee


#endif // DEREFEREE_PLATFORM_H
