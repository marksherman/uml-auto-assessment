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

#ifndef POLYMORPHISM_TESTS_H
#define POLYMORPHISM_TESTS_H

#include <dereferee.h>
#include "test_suite.h"

#include "test_support.h"

using namespace Dereferee;

/*
 * Test cases involving pointers to polymorphic objects and virtual function
 * dispatching.
 */
DT_BEGIN_SUITE(polymorphism_tests)

	/* setup */
	checked(Derived*) pDerived = new Derived(4);
	bool success = false;

	DT_TEST(derived_to_base_initialization)
	{
		DT_ASSERT_SUCCESS({
			checked(Base*) p = pDerived;
			success = (p->tag() == 16) && (p == pDerived);
		})
		DT_ASSERT_CONDITION(success)
	}

	DT_TEST(derived_to_base_assignment)
	{
		DT_ASSERT_SUCCESS({
			checked(Base*) p;
			p = pDerived;
			success = (p->tag() == 16) && (p == pDerived);
		})
		DT_ASSERT_CONDITION(success)
	}
	
	delete pDerived;

DT_END_SUITE()

#endif // ARRAY_INDEXING_TESTS_H
