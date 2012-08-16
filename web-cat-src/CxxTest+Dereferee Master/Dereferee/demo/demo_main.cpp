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
#include <limits>
#include <dereferee.h>

#include "demo_framework.h"

using namespace std;

// ===========================================================================

static void print_banner();
static int show_menu_and_get_choice();
static int read_number_from_cin();
static void dereference_null_pointer();
static void dereference_uninitialized_pointer();
static void double_delete_pointer();
static void wrong_array_delete();
static void array_out_of_bounds();
static void memory_leaks();
static void arithmetic_move_out_of_bounds();
static void arithmetic_deref_out_of_bounds();

// ===========================================================================

/** Signature for a menu item callback function. */
typedef void (* menu_choice_handler)();

/** Specifies the label and handler function for a menu item. */
struct menu_choice
{
	const char* label;
	menu_choice_handler handler;
};

/** The menu items displayed by the demo application. */
static menu_choice menu_choices[] = {
	{ "Dereference a null pointer",
		dereference_null_pointer },
	{ "Dereference an uninitialized pointer",
		dereference_uninitialized_pointer },
	{ "Delete a pointer twice",
		double_delete_pointer },
	{ "Delete an array with delete instead of delete[]",
		wrong_array_delete },
	{ "Access an out-of-bounds element in an array",
		array_out_of_bounds },
	{ "Move a pointer out-of-bounds using arithmetic",
		arithmetic_move_out_of_bounds },
	{ "Dereference an out-of-bounds pointer",
		arithmetic_deref_out_of_bounds },
	{ "Generate some memory leaks",
		memory_leaks }
};

/** The number of menu items available in the demo. */
static const int num_menu_choices =
	sizeof(menu_choices) / sizeof(menu_choice);

static const char* SEPARATOR =
	"======================================================================";

// ===========================================================================

int main(int /*argc*/, char** /*argv*/)
{
	print_banner();

	int choice = show_menu_and_get_choice();

	if (choice != -1)
		menu_choices[choice].handler();

	return 0;
}

// ---------------------------------------------------------------------------
static void print_banner()
{
	cout
	<< SEPARATOR
	<< endl
	<< "Dereferee interactive demo"
	<< endl
	<< "http://web-cat.org/"
	<< endl
	<< SEPARATOR
	<< endl
	<< "This demo application lets you choose from a number of pointer errors"
	<< endl
	<< "to execute, so you can see the results that Dereferee generates upon"
	<< endl
	<< "failure."
	<< endl
	<< endl
	<< "Please see the accompanying README file for information about the"
	<< endl
	<< "behavior of this demo with respect to recoverable and unrecoverable"
	<< endl
	<< "errors."
	<< endl;
}

// ---------------------------------------------------------------------------
static int read_number_from_cin()
{
	int number = -1;
	cin >> number;

	if(!cin)
	{
		number = -1;
		cin.clear();
		cin.ignore(numeric_limits<streamsize>::max(), '\n');
	}
	
	return number;
}

// ---------------------------------------------------------------------------
static int show_menu_and_get_choice()
{
	cout
	<< SEPARATOR
	<< endl
	<< "Please choose one of the errors below to see its output:"
	<< endl;
	
	for (int i = 0; i < num_menu_choices; i++)
	{
		cout << "  " << setw(2) << (i + 1) << ") "
			<< menu_choices[i].label << endl;
	}
	
	cout
	<< endl
	<< "Enter your choice (1-" << num_menu_choices << ", or 0 to quit): ";

	int choice = read_number_from_cin();

	while (choice < 0 || choice > num_menu_choices)
	{
		cout
		<< endl
		<< "Invalid selection."
		<< endl
		<< "Enter your choice (1-" << num_menu_choices << ", or 0 to quit): ";

		choice = read_number_from_cin();
	}

	return (choice - 1);
}

// ---------------------------------------------------------------------------
static void dereference_null_pointer()
{
	PREPARE_TO_PRINT; PRINT_AND_EXECUTE({
		checked(int*) p = NULL;
		*p = 5;
	});
}

// ---------------------------------------------------------------------------
static void dereference_uninitialized_pointer()
{
	PREPARE_TO_PRINT; PRINT_AND_EXECUTE({
		checked(int*) p;
		*p = 5;
	});
}

// ---------------------------------------------------------------------------
static void double_delete_pointer()
{
	PREPARE_TO_PRINT; PRINT_AND_EXECUTE({
		checked(int*) p = new int;
		delete p;
		delete p;
	});
}

// ---------------------------------------------------------------------------
static void wrong_array_delete()
{
	PREPARE_TO_PRINT; PRINT_AND_EXECUTE({
		checked(int*) p = new int[5];
		delete p;
	});
}

// ---------------------------------------------------------------------------
static void array_out_of_bounds()
{
	PREPARE_TO_PRINT; PRINT_AND_EXECUTE({
		checked(int*) p = new int[5];
		p[5] = 20;
		delete [] p;
	});
}

// ---------------------------------------------------------------------------
static void memory_leaks()
{
	PREPARE_TO_PRINT; PRINT_AND_EXECUTE({
		checked(int*) p1 = new int[10];
		checked(std::string*) p2 = new std::string;
	});
}

// ---------------------------------------------------------------------------
static void arithmetic_move_out_of_bounds()
{
	PREPARE_TO_PRINT; PRINT_AND_EXECUTE({
		checked(int*) p1 = new int[10];
		checked(int*) p2 = p1;
		p1 += 10;	// one-past-the-end is valid to *exist*
		p1++;		// but beyond that is not
		delete [] p2;
	});
}

// ---------------------------------------------------------------------------
static void arithmetic_deref_out_of_bounds()
{
	PREPARE_TO_PRINT; PRINT_AND_EXECUTE({
		checked(int*) p1 = new int[10];
		checked(int*) p2 = p1;
		p1 += 10;	// one-past-the-end is valid to *exist*
		*p1 = 10;	// but *not* to dereference
		delete [] p2;
	});
}
