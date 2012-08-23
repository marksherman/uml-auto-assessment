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

#ifndef MEMORY_LEAK_TESTS_H
#define MEMORY_LEAK_TESTS_H

#include <dereferee.h>
#include "test_suite.h"

using namespace Dereferee;

/*
 * Tests involving memory leaks.
 */
DT_BEGIN_SUITE(memory_leak_tests)

	DT_TEST(leak_out_of_scope)
	{
		DT_ASSERT_WARNING(warning_live_pointer_out_of_scope,
		{
			checked(int*) p = new int;
		});
	}

	DT_TEST(leak_overwritten)
	{
		DT_ASSERT_WARNING(warning_live_pointer_overwritten,
		{
			checked(int*) p = new int;
			p = NULL;
		});
	}

	DT_TEST(no_leak_out_of_scope)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) p = new int;
			delete p;
		});
	}

	DT_TEST(no_leak_overwritten)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(int*) p = new int;
			delete p;
			p = NULL;
		});
	}

DT_END_SUITE()

#endif // MEMORY_LEAK_TESTS_H
