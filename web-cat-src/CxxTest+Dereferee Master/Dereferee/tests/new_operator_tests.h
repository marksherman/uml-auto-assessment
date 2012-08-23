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

#ifndef NEW_OPERATOR_TESTS_H
#define NEW_OPERATOR_TESTS_H

#include <memory>
#include <new>
#include <dereferee.h>
#include "test_suite.h"

using namespace Dereferee;

/*
 * Test cases involving various forms of new.
 */

DT_BEGIN_SUITE(new_operator_tests)

	/*
	 * Attempt a very large allocation that malloc will not be able to handle.
	 * The parameterless new operator should throw a std::bad_alloc exception.
	 */
	DT_TEST(new_throw_overflow)
	{
		DT_ASSERT_THROWS(std::bad_alloc,
		{
			checked(char*) p = new char[(size_t)(0x7FFFFFFF)];
		})
	}

	/*
	 * Attempt a very large allocation that malloc will not be able to handle.
	 * The parameterized new operator should return NULL.
	 */
	DT_TEST(new_nothrow_overflow)
	{
		DT_ASSERT_CONDITION( new(std::nothrow) char[(size_t)(0x7FFFFFFF)] == NULL )
	}

DT_END_SUITE()

#endif // NEW_OPERATOR_TESTS_H
