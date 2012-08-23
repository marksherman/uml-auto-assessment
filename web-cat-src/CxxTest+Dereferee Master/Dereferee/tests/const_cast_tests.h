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

#ifndef CONST_CAST_TESTS_H
#define CONST_CAST_TESTS_H

#include <dereferee.h>
#include "test_suite.h"

#include "test_support.h"

using namespace Dereferee;

/*
 * Test cases involving the enhanced const_cast operator.
 */
DT_BEGIN_SUITE(const_cast_tests)

	/* setup */
	int* p = new int(5);
	const int* cp = p;
	checked(int*) pChecked = p;
	checked(const int*) cpChecked = p;

	int* temp;
	checked(int*) tempChecked;

	int n = 5;
	const int& cnr = n;

	bool success = false;
	
	/*
	 * Verify that a const_cast between raw pointers still works with the new
	 * macro/templates.
	 */
	DT_TEST(raw_to_raw_cast)
	{
		DT_ASSERT_CONDITION((
			temp = const_cast<int*>(cp),
			(temp == cp && *temp == 5)
		))
	}
	
	/*
	 * Verify that a const_cast from a raw pointer to a checked pointer works.
	 */
	DT_TEST(raw_to_checked_cast)
	{
		DT_ASSERT_CONDITION((
			tempChecked = const_cast<int*>(cp),
			(tempChecked == cp && *tempChecked == 5)
		))
	}
	
	/*
	 * Verify that a const_cast from a checked pointer to a raw pointer works.
	 */
	DT_TEST(checked_to_raw_cast)
	{
		DT_ASSERT_CONDITION((
			temp = const_cast<int*>(cpChecked),
			(temp == cpChecked && *temp == 5)
		))
	}
	
	/*
	 * Verify that a const_cast between checked pointers works.
	 */
	DT_TEST(checked_to_checked_cast)
	{
		DT_ASSERT_CONDITION((
			tempChecked = const_cast<int*>(cpChecked),
			(tempChecked == cpChecked && *tempChecked == 5)
		))
	}
	
	/*
	 * Verify that reference casting still works with the new macro/templates.
	 */
	DT_TEST(reference_cast)
	{
		DT_ASSERT_SUCCESS({
			int& ref = const_cast<int&>(cnr);
			success = (&ref == &cnr && ref == 5);
		})
		DT_ASSERT_CONDITION(success)
	}
	
	/*
	 * Verify that a non-lvalue can be passed into the new macro/templates.
	 */
	DT_TEST(direct_from_new_cast)
	{
		DT_ASSERT_SUCCESS({
			checked(int*) p2 = const_cast<int*>(new int);
			delete p2;
		})
	}

	/* tear down */
	delete p;

DT_END_SUITE()

#endif // CONST_CAST_TESTS_H
