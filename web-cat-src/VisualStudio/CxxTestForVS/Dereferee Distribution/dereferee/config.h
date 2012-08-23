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

#ifndef DEREFEREE_CONFIG_H
#define DEREFEREE_CONFIG_H

/*
 * Each of the following is a feature of Dereferee that requires conditional
 * compilation to be cleanly supported or gracefully omitted on various
 * compilers. If your compiler is not listed here, you can add it by using
 * preprocessor macros to detect its existence and version, and enabling or
 * disabling the features that the compiler supports.
 */

/*
 * DEREFEREE_CONFIG_ENHANCED_DECLARATION
 * Value: defined/undefined
 *
 * Does the compiler support variadic macros? If so, we allow declaration of
 * checked pointers to take the form checked(T*). If not, clients must use the
 * checked pointer class directly, in the form checked_ptr<T*>.
 *
 * ----
 * DEREFEREE_THROW_BAD_ALLOC
 * Value: throw(std::bad_alloc)/undefined
 *
 * Does the compiler support exception specifications? If so, we indicate that
 * operator new and various helper methods can throw std::bad_alloc using an
 * exception specification. For compilers that ignore these, we use an empty
 * definition to leave it out.
 *
 * ----
 * DEREFEREE_NO_DYNAMIC_CAST
 * Value: defined/undefined
 *
 * Does the compiler support partial template specializations well enough to
 * use our redefinition of the dynamic_cast operator? If not, define this and
 * those definitions will not be included; however, any code that passes a
 * checked_ptr<T*> to dynamic_cast will first have to explicitly cast it back
 * to T*.
 *
 * ----
 * DEREFEREE_STRTOK
 * Value: strtok_r/strtok_s
 *
 * Which strtok function does this compiler support?  On Unix, Mac OS X, and
 * Cygwin, we use strtok_r. On Microsoft Visual C++, we use strtok_s.
 */


// GCC 3.0 and above:
#if(defined(__GNUC__) && __GNUC__ >= 3)
#	define DEREFEREE_CONFIG_ENHANCED_DECLARATION
#	define DEREFEREE_THROW_BAD_ALLOC throw(std::bad_alloc)
#	define DEREFEREE_STRTOK strtok_r
#endif

// Microsoft Visual C++ 2005 and above:
#if(defined(_MSC_VER) && _MSC_VER >= 1400)
#	define DEREFEREE_CONFIG_ENHANCED_DECLARATION
#	define DEREFEREE_THROW_BAD_ALLOC
#	define DEREFEREE_STRTOK strtok_s
	// Eliminate warnings from C standard library functions that Microsoft
	// calls "unsafe"
#	define _CRT_SECURE_NO_DEPRECATE 1
#endif


#endif // DEREFEREE_CONFIG_H
