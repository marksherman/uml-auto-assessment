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

#ifndef ARITHMETIC_TESTS_H
#define ARITHMETIC_TESTS_H

#include <memory>
#include <new>
#include <dereferee.h>
#include "test_suite.h"

using namespace Dereferee;

/*
 * Test cases involving pointer arithmetic and bounds checking.
 */

DT_BEGIN_SUITE(arithmetic_tests)

	/* setup */
	const size_t ARRAY_SIZE = 3;
	int* array = new int[ARRAY_SIZE];
	for(size_t i = 0; i < ARRAY_SIZE; i++) array[i] = i;
	
	checked(int*) arrayChecked = array;
	bool success = false;

	int* array2 = new int[ARRAY_SIZE];

	/*
	 * Decrement a pointer out of bounds.
	 */
	DT_TEST(decrement_out_of_bounds)
	{
		DT_ASSERT_ERROR(error_arithmetic_moved_out_of_bounds,
		{
			checked(int*) temp = arrayChecked;
			temp--;
		})
	}

	/*
	 * Subtract a pointer out of bounds.
	 */
	DT_TEST(subtract_out_of_bounds)
	{
		DT_ASSERT_ERROR(error_arithmetic_moved_out_of_bounds,
		{
			checked(int*) temp = arrayChecked;
			temp = temp - 4;
		})
	}

	/*
	 * Make sure a pointer remains valid when increment all the way to one
	 * element past the end of the array, and then decremented back to the
	 * beginning.
	 */
	DT_TEST(valid_inc_dec_sequence)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) temp = arrayChecked;
			temp++; temp++; temp++;
			temp--; temp--; temp--;
		})
	}

	/*
	 * Same as above, but with integer operations instead of
	 * increment/decrement.
	 */
	DT_TEST(valid_add_sub_sequence)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) temp = arrayChecked;
			temp += 3;
			temp -= 3;
		})
	}

	/*
	 * Verify that valid elements in the array can be dereferenced after
	 * repeated in-bounds arithmetic.
	 */
	DT_TEST(valid_dereference_sequence)
	{
		DT_ASSERT_SUCCESS(
		{
			success = true;
			checked(int*) temp = arrayChecked;
			int i = 0;
			
			while(temp != arrayChecked + 3)
				success &= (*temp++ == i++);
		})
		DT_ASSERT_CONDITION(success)
	}

	/*
	 * A pointer to one-past-the-end of an array is valid, but dereferencing it
	 * is not, so verify that it throws an error.
	 */
	DT_TEST(dereference_one_past_end)
	{
		DT_ASSERT_ERROR(error_deref_out_of_bounds_star_op,
		{
			checked(int*) temp = arrayChecked;
			temp++; temp++; temp++;
			*temp = 4;
		})
	}

	/*
	 * Initialize a pointer to the middle of the array and verify that the
	 * valid indices are -1, 0, and 1.
	 */
	DT_TEST(offset_array_indices)
	{
		checked(int*) temp = arrayChecked + 1;
		
		DT_ASSERT_CONDITION( temp[-1] == 0 )
		DT_ASSERT_CONDITION( temp[0] == 1 )
		DT_ASSERT_CONDITION( temp[1] == 2 )
		DT_ASSERT_ERROR(error_index_out_of_bounds, { temp[-2] = 0; })
		DT_ASSERT_ERROR(error_index_out_of_bounds, { temp[2] = 0; })
	}
	
	/*
	 * Compute the distance between two pointers. Verify that it works for
	 * several combinations of in-bounds and one-past-the-end pointers.
	 */
	DT_TEST(pointer_distance)
	{
		checked(int*) p1;
		checked(int*) p2;
		DT_ASSERT_CONDITION( (p1 = arrayChecked, p2 = arrayChecked, p2 - p1 == 0) )
		DT_ASSERT_CONDITION( (p1 = arrayChecked, p2 = arrayChecked + 1, p2 - p1 == 1) )
		DT_ASSERT_CONDITION( (p1 = arrayChecked, p2 = arrayChecked + 1, p1 - p2 == -1) )
		DT_ASSERT_CONDITION( (p1 = arrayChecked, p2 = arrayChecked + 3, p2 - p1 == 3) )
		DT_ASSERT_CONDITION( (p1 = arrayChecked, p2 = arrayChecked + 3, p1 - p2 == -3) )
	}

	/*
	 * Verify that an error occurs if the pointers being subtracted are not in
	 * the same memory block or when one is NULL.
	 */
	DT_TEST(pointer_distance_different_blocks)
	{
		checked(int*) p1;
		checked(int*) p2;
		DT_ASSERT_ERROR(error_subtraction_different_blocks,
		{
			p1 = array;
			p2 = array2;
			p2 - p1;
		})
		DT_ASSERT_ERROR(error_subtraction_one_side_null,
		{
			p1 = array;
			p2 = NULL;
			p1 - p2;
		})
		DT_ASSERT_ERROR(error_subtraction_one_side_null,
		{
			p1 = array;
			p2 = NULL;
			p2 - p1;
		})		
	}

	/* tear down */
	delete[] array;
	delete[] array2;

DT_END_SUITE()

#endif // ARITHMETIC_TESTS_H
