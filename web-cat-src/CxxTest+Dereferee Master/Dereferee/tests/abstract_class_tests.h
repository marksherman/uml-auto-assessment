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

#ifndef ABSTRACT_CLASS_TESTS_H
#define ABSTRACT_CLASS_TESTS_H

#include <memory>
#include <new>
#include <dereferee.h>
#include "test_suite.h"
#include "test_support.h"

using namespace Dereferee;

/*
 * Test cases involving abstract classes.
 */

DT_BEGIN_SUITE(abstract_class_tests)

	/*
	 * Allocate an object.
	 */
	DT_TEST(allocate_object)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(AbstractBase*) p = new AbstractBaseSubclass;
			p->method();
			delete p;
		})
	}

	/*
	 * Dereference the pointer with the star operator.
	 */
	DT_TEST(allocate_array)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(AbstractBase*) p = new AbstractBaseSubclass[5];
			delete [] p;
		})
	}

	/*
	 * Dereference the pointer with the star operator.
	 */
	DT_TEST(dereference_star)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(AbstractBase*) p = new AbstractBaseSubclass;
			(*p).method();
			delete p;
		})
	}

	/*
	 * Dereference the pointer with the arrow operator.
	 */
	DT_TEST(dereference_arrow)
	{
		DT_ASSERT_SUCCESS(
		{
			checked(AbstractBase*) p = new AbstractBaseSubclass;
			p->method();
			delete p;
		})
	}

DT_END_SUITE()

#endif // ABSTRACT_CLASS_TESTS_H
