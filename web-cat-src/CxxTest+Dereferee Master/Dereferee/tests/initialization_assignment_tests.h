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

#ifndef INITIALIZATION_ASSIGNMENT_TESTS_H
#define INITIALIZATION_ASSIGNMENT_TESTS_H

#include <dereferee.h>
#include "test_suite.h"

using namespace Dereferee;

/*
 * Tests involving initialization and assignment of checked pointers.
 */
DT_BEGIN_SUITE(initialization_assignment_tests)

	DT_TEST(uninitialized)
	{
		DT_ASSERT_SUCCESS( { checked(int*) p; } );
	}

	DT_TEST(null_raw_initialization)
	{
		DT_ASSERT_SUCCESS( { checked(int*) p = NULL; } );
	}

	DT_TEST(null_checked_initialization)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) p = NULL;
			checked(int*) q = p;
		});
	}

	DT_TEST(literal_zero_initialization)
	{
		DT_ASSERT_SUCCESS( { checked(int*) p = 0; } );
	}
	
	DT_TEST(dead_uninitialized_initialization)
	{
		DT_ASSERT_ERROR(error_assign_dead_uninitialized,
		{
			checked(int*) p;
			checked(int*) q = p;
		});
	}

	DT_TEST(dead_deleted_initialization)
	{
		DT_ASSERT_ERROR(error_assign_dead_deleted,
		{
			checked(int*) p = new int;
			delete p;
			checked(int*) q = p;
		});
	}

	DT_TEST(live_raw_direct_initialization)
	{
		DT_ASSERT_SUCCESS( {
			checked(int*) p = new int;
			delete p;
		} );
	}

	DT_TEST(live_raw_initialization)
	{
		DT_ASSERT_SUCCESS(
		{
			int* p = new int;
			checked(int*) q = p;
			delete q;
		});
	}

	DT_TEST(live_checked_initialization)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) p = new int;
			checked(int*) q = p;
			delete p;
		});
	}

	/* The following tests are the same as those above, but using assignment
	 * to an already declared checked pointer as opposed to initialization. */

	DT_TEST(null_raw_assignment)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) p;
			p = NULL;
		});
	}

	DT_TEST(literal_zero_assignment)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) p;
			p = 0;
		});
	}
	
	DT_TEST(dead_uninitialized_assignment)
	{
		DT_ASSERT_ERROR(error_assign_dead_uninitialized,
		{
			checked(int*) p;
			checked(int*) q;
			q = p;
		});
	}

	DT_TEST(dead_deleted_assignment)
	{
		DT_ASSERT_ERROR(error_assign_dead_deleted,
		{
			checked(int*) p;
			p = new int;
			delete p;
			checked(int*) q;
			q = p;
		});
	}

	DT_TEST(live_raw_direct_assignment)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) p;
			p = new int;
			delete p;
		});
	}

	DT_TEST(live_raw_assignment)
	{
		DT_ASSERT_SUCCESS(
		{
			int* p = new int;
			checked(int*) q;
			q = p;
			delete q;
		});
	}

	DT_TEST(live_checked_assignment)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) p;
			p = new int;
			checked(int*) q;
			q = p;
			delete p;
		});
	}

DT_END_SUITE()

#endif // INITIALIZATION_ASSIGNMENT_TESTS_H
