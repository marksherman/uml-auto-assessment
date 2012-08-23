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

#ifndef CONST_CORRECTNESS_TESTS_H
#define CONST_CORRECTNESS_TESTS_H

#include <dereferee.h>
#include "test_suite.h"

using namespace Dereferee;

/*
 * Test cases involving comparisons (equality, inequality, and relational
 * operators).
 */
class int_wrapper
{
private:
	int value;
	
public:
	int_wrapper(int v = 20) : value(v) { }
	operator int() const { return value; }
};

DT_BEGIN_SUITE(const_correctness_tests)

	DT_TEST(const_dereferencing)
	{
		checked(int*) p1 = new int(1);
		checked(const int*) cp1 = new int(3);
		checked(int* const) pc1 = new int(5);
		checked(const int* const) cpc1 = new int(10);

		DT_ASSERT_CONDITION( *p1 == 1 );
		DT_ASSERT_CONDITION( *cp1 == 3 );
		DT_ASSERT_CONDITION( *pc1 == 5 );
		DT_ASSERT_CONDITION( *cpc1 == 10 );

		delete p1;
		delete cp1;
		delete pc1;
		delete cpc1;
	}

	DT_TEST(const_array_indexing)
	{
		checked(int_wrapper*) p1 = new int_wrapper[1];
		checked(const int_wrapper*) cp1 = new int_wrapper[1];
		checked(int_wrapper* const) pc1 = new int_wrapper[1];
		checked(const int_wrapper* const) cpc1 = new int_wrapper[1];

		DT_ASSERT_CONDITION( p1[0] == 20 );
		DT_ASSERT_CONDITION( cp1[0] == 20 );
		DT_ASSERT_CONDITION( pc1[0] == 20 );
		DT_ASSERT_CONDITION( cpc1[0] == 20 );
	
		delete[] p1;
		delete[] cp1;
		delete[] pc1;
		delete[] cpc1;
	}

DT_END_SUITE()

#endif // CONST_CORRECTNESS_TESTS_H
