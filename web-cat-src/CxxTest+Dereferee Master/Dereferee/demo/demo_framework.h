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

#ifndef DEREFEREE_DEMO_FRAMEWORK_H
#define DEREFEREE_DEMO_FRAMEWORK_H

// ===========================================================================

/**
 * Stores the line number before printing and executing a block of demo
 * code. This is required as a separate macro because (at least under some)
 * compilers, __LINE__ evaluates to the *last* line of a multi-line macro
 * expansion, rather than the *first*.
 */ 
#define PREPARE_TO_PRINT _pae_line = __LINE__

/**
 * Prints, then executes, the specified block of code.
 */
#define PRINT_AND_EXECUTE(block) \
	pretty_print_code(#block); \
	prompt_user_to_press_enter(); \
	block

extern int _pae_line;

void pretty_print_code(const char* code);
void prompt_user_to_press_enter();

#endif // DEREFEREE_DEMO_FRAMEWORK_H
