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

#ifndef COMPARISON_TESTS_H
#define COMPARISON_TESTS_H

#include <dereferee.h>
#include "test_suite.h"

using namespace Dereferee;

/*
 * Test cases involving comparisons (equality, inequality, and relational
 * operators).
 */
DT_BEGIN_SUITE(comparison_tests)

	/* setup */
	checked(int*) array1 = new int[10];
	checked(int*) array2 = new int[10];
	int* raw3 = new int[10];
	int* raw4 = new int[10];
	checked(int*) checked3 = raw3;
	checked(int*) uninit;
	checked(int*) deleted = new int; delete deleted;

	/*
	 * Verify that equality/inequality operators return the proper results
	 * when the pointers point to the same address.
	 */
	DT_TEST(self_equality)
	{
		DT_ASSERT_CONDITION( array1 == array1 )
		DT_ASSERT_CONDITION( !(array1 != array1) )
	}

	/*
	 * Verify that equality/inequality operators return the proper results
	 * when the pointers point to different addresses.
	 */
	DT_TEST(inequality_of_different_pointers)
	{
		DT_ASSERT_CONDITION( !(array1 == array2) )
		DT_ASSERT_CONDITION( array1 != array2 )
	}
	
	/*
	 * Verify that the equality/inequality operators return the proper results
	 * when a raw pointer is being compared to a checked pointer that points
	 * to the same block.
	 */
	DT_TEST(raw_checked_equality)
	{
		DT_ASSERT_CONDITION( raw3 == checked3 )
		DT_ASSERT_CONDITION( checked3 == raw3 )
		DT_ASSERT_CONDITION( !(raw3 != checked3) )
		DT_ASSERT_CONDITION( !(checked3 != raw3) )
	}

	/*
	 * Verify that the equality/inequality operators return the proper results
	 * when a raw pointer is being compared to a checked pointer that points
	 * to a different block.
	 */
	DT_TEST(raw_checked_inequality)
	{
		DT_ASSERT_CONDITION( raw4 != checked3 )
		DT_ASSERT_CONDITION( checked3 != raw4 )
		DT_ASSERT_CONDITION( !(raw4 == checked3) )
		DT_ASSERT_CONDITION( !(checked3 == raw4) )
	}

	/*
	 * Verify that the equality/inequality operators throw an error if one
	 * of the arguments is uninitialized.
	 */
	DT_TEST(compare_uninitialized)
	{
		DT_ASSERT_ERROR(error_compare_dead_uninitialized,
						{ array1 == uninit; });
		
		DT_ASSERT_ERROR(error_compare_dead_uninitialized,
						{ array1 != uninit; });
		
		DT_ASSERT_ERROR(error_compare_dead_uninitialized,
						{ uninit == array2; });
		
		DT_ASSERT_ERROR(error_compare_dead_uninitialized,
						{ uninit != array2; });
	}

	/*
	 * Verify that the equality/inequality operators throw an error if one
	 * of the arguments has been deleted.
	 */
	DT_TEST(compare_deleted)
	{
		DT_ASSERT_ERROR(error_compare_dead_deleted,
						{ array1 == deleted; });
		
		DT_ASSERT_ERROR(error_compare_dead_deleted,
						{ array1 != deleted; });
		
		DT_ASSERT_ERROR(error_compare_dead_deleted,
						{ deleted == array2; });
		
		DT_ASSERT_ERROR(error_compare_dead_deleted,
						{ deleted != array2; });
	}

	/*
	 * Verify that the relational operators return the proper results when the
	 * pointers are in the same blocks.
	 */
	DT_TEST(relational_same_array)
	{
		checked(int*) advanced = &array1[1];

		DT_ASSERT_CONDITION( array1 < advanced );
		DT_ASSERT_CONDITION( array1 <= advanced );
		DT_ASSERT_CONDITION( !(array1 > advanced) );
		DT_ASSERT_CONDITION( !(array1 >= advanced) );
	}

	/*
	 * Verify that the relational operators throw an error if the pointers
	 * are in different blocks.
	 */
	DT_TEST(relational_different_arrays)
	{
		DT_ASSERT_ERROR(error_relational_different_blocks,
						{ array1 < array2; });

		DT_ASSERT_ERROR(error_relational_different_blocks,
						{ array1 <= array2; });

		DT_ASSERT_ERROR(error_relational_different_blocks,
						{ array1 > array2; });

		DT_ASSERT_ERROR(error_relational_different_blocks,
						{ array1 >= array2; });
	}

	/* tear down */
	delete[] array1;
	delete[] array2;
	delete[] raw3;
	delete[] raw4;

DT_END_SUITE()

#endif // COMPARISON_TESTS_H
