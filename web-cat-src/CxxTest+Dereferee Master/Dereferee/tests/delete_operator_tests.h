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

#ifndef DELETE_OPERATOR_TESTS_H
#define DELETE_OPERATOR_TESTS_H

#include <memory>
#include <new>
#include <dereferee.h>
#include "test_suite.h"

using namespace Dereferee;

/*
 * Test cases involving various forms of new and delete.
 */

DT_BEGIN_SUITE(delete_operator_tests)

	/*
	 * Attempt to delete NULL -- this is acceptable and should pass without
	 * warning or error.
	 */
	DT_TEST(delete_null)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) p = NULL; delete p; delete[] p;
		})
	}
	/*
	 * Attempt to delete a pointer twice.
	 */
	DT_TEST(delete_twice)
	{
		DT_ASSERT_ERROR(error_used_dead_deleted,
		{
			checked(int*) p = new int;
			delete p;
			delete p;
		})
	}
	
	/*
	 * Not a checked pointer test per se, but the overloaded delete operator
	 * should catch these errors.
	 */
	DT_TEST(delete_stack)
	{
		DT_ASSERT_ERROR(error_nonarray_delete_dead_deleted,
		{
			int x = 5; int* p = &x; delete p;
		})
		DT_ASSERT_ERROR(error_nonarray_delete_dead_deleted,
		{
			int x = 5; int* p = &x; delete p;
		})
	}

DT_END_SUITE()

#endif // DELETE_OPERATOR_TESTS_H
