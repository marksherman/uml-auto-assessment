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

#ifndef ARRAY_INDEXING_TESTS_H
#define ARRAY_INDEXING_TESTS_H

#include <dereferee.h>
#include "test_suite.h"

using namespace Dereferee;

/*
 * Test cases involving array indexing.
 */
DT_BEGIN_SUITE(array_indexing_tests)

	/* setup */
	const size_t ARRAY_SIZE = 10;
	int* raw_array = new int[ARRAY_SIZE];
	for(size_t i = 0; i < ARRAY_SIZE; i++) raw_array[i] = i;
	
	checked(int*) array = raw_array;
	checked(int*) non_array = new int;

	DT_TEST(array_simple)
	{
		DT_ASSERT_CONDITION( (array[0] = 5, array[0] == 5) );
	}
	
	array[0] = 0;		// Restore old value.

	DT_TEST(non_array_index)
	{
		DT_ASSERT_ERROR(error_index_non_array, { non_array[0] = 10; } )
	}

	DT_TEST(full_range)
	{
		for(int i = 0; i < (int)ARRAY_SIZE; i++)
		{
			DT_ASSERT_CONDITION( array[i] == i );
		}
	}

	DT_TEST(negative_index_out_of_bounds)
	{
		DT_ASSERT_ERROR(error_index_out_of_bounds, { array[-1] = 10; } )
	}

	DT_TEST(negative_index_out_of_bounds)
	{
		DT_ASSERT_ERROR(error_index_out_of_bounds, { array[-1] = 10; } )
	}

	DT_TEST(positive_index_just_out_of_bounds)
	{
		DT_ASSERT_ERROR(error_index_out_of_bounds, { array[10] = 10; } )
	}

	DT_TEST(positive_index_way_out_of_range)
	{
		DT_ASSERT_ERROR(error_index_out_of_bounds, { array[100] = 10; } )
	}

	/* tear down */
	delete[] array;
	delete non_array;

DT_END_SUITE()

#endif // ARRAY_INDEXING_TESTS_H
