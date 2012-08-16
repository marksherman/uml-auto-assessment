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

/*
 * ACKNOWLEDGMENTS:
 * This code is based on and extends the work done by Scott M. Pike and
 * Bruce W. Weide at The Ohio State University and Joseph E. Hollingsworth of
 * Indiana University SE in "Checkmate: Cornering C++ Dynamic Memory Errors
 * With Checked Pointers", Proc. of the 31st SIGCSE Technical Symposium on
 * CSE, ACM Press, March 2000.
 */

#ifndef DEREFEREE_H
#define DEREFEREE_H

#include "dereferee/config.h"

// ===========================================================================
/*
 * Dereferee provides two syntaxes for declaring checked pointers. The first,
 * checked(T*), is preferred, but only supported on C++ compilers that support
 * variadic macros (as of this writing, GCC 3.0+, Visual C++ 2005,
 * C++ Builder 2006).  Variadic macro support is required so that pointers to
 * template types with multiple arguments, i.e. checked(Template<T, U>*), are
 * handled properly by the preprocessor, because the comma in this case is
 * still interpreted as an argument separator.
 *
 * The benefit of this syntax is that the checked pointers can be entirely
 * compiled out by defining the preprocessor macro DEREFEREE_DISABLED, which
 * nullifies checked(T*) to simply T*.
 *
 * In the event that the name "checked" clashes with existing code, define
 * DEREFEREE_UNDERSCORE_DECLARATOR in the preprocessor and the macro will be
 * defined as "__checked" instead of "checked".
 *
 * If you are using a compiler that does not support variadic macros, you can
 * instead declare checked pointers using their class directly:
 * checked_ptr<T*>. The drawback to this syntax is that since it is not a
 * preprocessor macro, the declarations cannot be entirely compiled out by
 * nullifying a macro definition. Instead, if DEREFEREE_DISABLED is defined in
 * this case, the checked_ptr<> class is replaced by a lightweight wrapper
 * class that contains no extra behavior.
 */

#ifdef DEREFEREE_CONFIG_ENHANCED_DECLARATION
#	ifdef DEREFEREE_UNDERSCORE_DECLARATOR
#		ifdef DEREFEREE_DISABLED
#			define __checked(...) __VA_ARGS__
#		else
#			define __checked(...) ::Dereferee::checked_ptr< __VA_ARGS__ >
#		endif
#	else
#		ifdef DEREFEREE_DISABLED
#			define checked(...) __VA_ARGS__
#		else
#			define checked(...) ::Dereferee::checked_ptr< __VA_ARGS__ >
#		endif
#	endif
#	include "dereferee/checked.h"
#else
#	ifdef DEREFEREE_DISABLED
#		include "dereferee/unchecked.h"
#	else
#		include "dereferee/checked.h"
#	endif
#endif

/*
 * Import only the declaration of checked_ptr into the global namespace. No
 * other classes in the Dereferee namespace are intended for public use, so in
 * order to prevent namespace pollution, users should not import anything else
 * from that namespace.
 */
using ::Dereferee::checked_ptr;

#endif // DEREFEREE_H
