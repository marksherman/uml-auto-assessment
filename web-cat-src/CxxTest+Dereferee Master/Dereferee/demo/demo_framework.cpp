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

#include <iostream>
#include <iomanip>
#include <cctype>
#include <csignal>
#include <limits>

#include "demo_framework.h"

using namespace std;

// ===========================================================================

int _pae_line;

static bool check_for_closing_brace(const char* code);
static const char* print_statement(const char* code);

// ===========================================================================

/**
 * Prints a block of code (stringified by the calling macro) with a single
 * statement (semicolon-delimited) per line, and with the leading and trailing
 * curly braces removed.
 */
void pretty_print_code(const char* code)
{
	cout
	<< "The following code will be executed (line numbers in demo_main.cpp):"
	<< endl << endl;

	// Get rid of the initial brace.
	if (*code == '{')
	{
		code += 2;
	}
	
	_pae_line++;

	while (!check_for_closing_brace(code))
	{
		cout << "(" << setw(3) << (_pae_line++) << ")    ";
		code = print_statement(code);
	}
	
	cout << endl;
}

// ---------------------------------------------------------------------------
/**
 * Prints a message and waits for the user to press Enter.
 */
void prompt_user_to_press_enter()
{
	cout << "Press enter to see the result.";

	cin.ignore(numeric_limits<streamsize>::max(), '\n');
	cin.get();

	cout << endl;
}

// ---------------------------------------------------------------------------
/**
 * Returns true if the specified string is the closing brace of a block of
 * code.
 */
static bool check_for_closing_brace(const char* code)
{
	return (*code == '}' && !*(code + 1));
}

// ---------------------------------------------------------------------------
/**
 * Prints a single statement (up to the next semicolon) from the specified
 * block of code, then returns a pointer to the next statement in that block.
 */
static const char* print_statement(const char* code)
{
	while(*code != ';')
	{
		cout << *(code++);
	}
	
	cout << *(code++) << endl;

	while(isspace(*code)) code++;

	return code;
}
