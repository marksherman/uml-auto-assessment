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

#include <cstdlib>
#include <cstdio>
#include <cstdarg>
#include <cstring>
#include "test_oriented_listener.h"

// ---------------------------------------------------------------------------
Dereferee::error_code test_oriented_listener::_last_error;
Dereferee::warning_code test_oriented_listener::_last_warning;

// ---------------------------------------------------------------------------
test_oriented_listener::test_oriented_listener()
{
	clear_errors();
}

// ---------------------------------------------------------------------------
test_oriented_listener::~test_oriented_listener()
{
}

// ------------------------------------------------------------------
size_t test_oriented_listener::maximum_leaks_to_report()
{
	return 0;
}

// ------------------------------------------------------------------
void test_oriented_listener::begin_report(
	const Dereferee::usage_stats& /*stats*/)
{
}

// ------------------------------------------------------------------
void test_oriented_listener::report_leak(
	const Dereferee::allocation_info& /*leak*/)
{
}		

// ------------------------------------------------------------------
void test_oriented_listener::report_truncated(
		size_t /*reports_logged*/, size_t /*actual_leaks*/)
{
}

// ------------------------------------------------------------------
void test_oriented_listener::end_report()
{
}

// ------------------------------------------------------------------
void test_oriented_listener::error(Dereferee::error_code code,
	va_list /*args*/)
{
	_last_error = code;

	// In Visual C++, the abort function prints extra gunk to the console,
	// so we just force a null pointer dereference instead to drop us into
	// the signal handler.
#ifdef _MSC_VER
	const int INTENTIONAL_NULL_POINTER_DEREFERENCE = 0;
	int* ptr = 0;
	*ptr = INTENTIONAL_NULL_POINTER_DEREFERENCE;
#else
	abort();
#endif
}

// ------------------------------------------------------------------
void test_oriented_listener::warning(Dereferee::warning_code code,
	va_list /*args*/)
{
	_last_warning = code;
}

// ------------------------------------------------------------------
void test_oriented_listener::clear_errors()
{
	_last_error = no_test_error;
	_last_warning = no_test_warning;
}

// ------------------------------------------------------------------
Dereferee::error_code test_oriented_listener::last_error()
{
	return _last_error;
}

// ------------------------------------------------------------------
Dereferee::warning_code test_oriented_listener::last_warning()
{
	return _last_warning;
}


// ===========================================================================
/*
 * Implementation of the functions called by the Dereferee memory manager to
 * create and destroy the listener object.
 */

// ---------------------------------------------------------------------------
Dereferee::listener* Dereferee::create_listener(
	const Dereferee::option* /*options*/, Dereferee::platform* /*platform*/ )
{
	return new test_oriented_listener;
}

// ---------------------------------------------------------------------------
void Dereferee::destroy_listener(Dereferee::listener* listener)
{
	delete listener;
}


// ===========================================================================
const char* error_messages[] =
{
	"Checked pointers cannot point to memory that wasn't allocated with new or new[]",
	"Assigned dead (never initialized) pointer to another pointer",
	"Assigned dead (already deleted) pointer to another pointer",
	"Assigned dead (out of bounds) pointer to another pointer",
	"Called delete instead of delete[] on array pointer",
	"Called delete[] instead of delete on non-array pointer",
	"Called delete on (never initialized) dead pointer",
	"Called delete[] on (never initialized) dead pointer",
	"Called delete on (already deleted or not dynamically allocated) dead pointer",
	"Called delete[] on (already deleted or not dynamically allocated) dead pointer",
	"Dereferenced (never initialized) dead pointer using operator->",
	"Dereferenced (never initialized) dead pointer using operator*",
	"Dereferenced (never initialized) dead pointer using operator[]",
	"Dereferenced (already deleted) dead pointer using operator->",
	"Dereferenced (already deleted) dead pointer using operator*",
	"Dereferenced (already deleted) dead pointer using operator[]",
	"Dereferenced (out of bounds) dead pointer using operator->",
	"Dereferenced (out of bounds) dead pointer using operator*",
	"Dereferenced (out of bounds) dead pointer using operator[]",
	"Dereferenced null pointer using operator->",
	"Dereferenced null pointer using operator*",
	"Dereferenced null pointer using operator[]",
	"Used (never initialized) dead pointer in an expression",
	"Used (already deleted) dead pointer in an expression",
	"Used (out of bounds) dead pointer in an expression",
	"Used (never initialized) dead pointer in a comparison",
	"Used (already deleted) dead pointer in a comparison",
	"Used (out of bounds) dead pointer in a comparison",
	"Used null pointer on only one side of an inequality comparison; if one side is null then the both sides must be null",
	"Both pointers being compared are alive but point into different memory blocks, so the comparison is undefined",
	"Used (never initialized) dead pointer in an arithmetic expression",
	"Used (already deleted) dead pointer in an arithmetic expression",
	"Used (out of bounds) dead pointer in an arithmetic expression",
	"Used null pointer in an arithmetic expression",
	"Used null pointer on only one side of a pointer subtraction expression; if one side is null then both sides must be null",
	"Both pointers being subtracted are alive but point into different memory blocks, so the distance between them is undefined",
	"Pointer arithmetic has moved a live pointer out of bounds",
	"Used operator[] on a pointer that does not point to an array",
	"Array index is out of bounds",
	"A previous operation has made this pointer invalid"
};

const char* warning_messages[] =
{
	"Memory leak caused by last live pointer to memory block going out of scope",
	"Memory leak caused by last live pointer to memory block being overwritten",
	"Memory around allocated block was corrupted, likely due to invalid array indexing or pointer arithmetic"
};
