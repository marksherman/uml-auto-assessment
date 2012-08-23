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

#ifndef DYNAMIC_CAST_TESTS_H
#define DYNAMIC_CAST_TESTS_H

#include <dereferee.h>
#include "test_suite.h"

#include "test_support.h"

using namespace Dereferee;

/*
 * Test cases involving the enhanced dynamic_cast operator.
 */
DT_BEGIN_SUITE(dynamic_cast_tests)

	/* setup */
	checked(Derived*) pDerivedChecked;
	checked(Base*) pBaseChecked = new Derived(4);
	checked(Base*) pJustBaseChecked = new Base(4);
	
	Derived* pDerived;
	Base* pBase = pBaseChecked;
	Base* pJustBase = pJustBaseChecked;
	
	Base localBase(4);
	Derived localDerived(4);
	Base& baseRef = localDerived;
	const Base& constBaseRef = localDerived;
	bool success = false;
	
	/*
	 * Verify that a dynamic_cast between raw pointers still works with the new
	 * macro/templates.
	 */
	DT_TEST(raw_to_raw_cast)
	{
		DT_ASSERT_CONDITION((
			pDerived = dynamic_cast<Derived*>(pBase),
			(pDerived == pBase && pDerived->tag() == 16)
		))
	}
	
	/*
	 * Verify that a dynamic_cast from a raw pointer to a checked pointer works.
	 */
	DT_TEST(raw_to_checked_cast)
	{
		DT_ASSERT_CONDITION((
			pDerivedChecked = dynamic_cast<Derived*>(pBase),
			(pDerivedChecked == pBase && pDerivedChecked->tag() == 16)
		))
	}
	
	/*
	 * Verify that a dynamic_cast from a checked pointer to a raw pointer works.
	 */
	DT_TEST(checked_to_raw_cast)
	{
		DT_ASSERT_CONDITION((
			pDerived = dynamic_cast<Derived*>(pBaseChecked),
			(pDerived == pBaseChecked && pDerived->tag() == 16)
		))
	}
	
	/*
	 * Verify that a dynamic_cast between checked pointers works.
	 */
	DT_TEST(checked_to_checked_cast)
	{
		DT_ASSERT_CONDITION((
			pDerivedChecked = dynamic_cast<Derived*>(pBaseChecked),
			(pDerivedChecked == pBaseChecked && pDerivedChecked->tag() == 16)
		))
	}
	
	/*
	 * Verify that a dynamic_cast returns NULL when it should.
	 */
	DT_TEST(bad_cast)
	{
		DT_ASSERT_CONDITION((
			pDerived = dynamic_cast<Derived*>(pJustBaseChecked),
			(pDerived == NULL)
		))
	}

	/*
	 * Verify that a dynamic_cast involving const pointers works properly.
	 */
	DT_TEST(const_pointer_cast)
	{
		checked(Derived* const) p = dynamic_cast<Derived* const>(pBase);
		DT_ASSERT_CONDITION( (p == pBase && p->tag() == 16) )
		checked(Derived* const) p2 = dynamic_cast<Derived*>(pBase);
		DT_ASSERT_CONDITION( (p2 == pBase && p2->tag() == 16) )
		checked(Derived*) p3 = dynamic_cast<Derived* const>(pBase);
		DT_ASSERT_CONDITION( (p2 == pBase && p2->tag() == 16) )
	}

	/*
	 * Verify that reference casting still works with the new macro/templates.
	 */
	DT_TEST(reference_cast)
	{
		DT_ASSERT_SUCCESS({
			Derived& derivedRef = dynamic_cast<Derived&>(baseRef);
			success = (&derivedRef == &localDerived && derivedRef.tag() == 16);
		})
		DT_ASSERT_CONDITION(success)
	}
	
	/*
	 * Verify that const reference casting still works with the new
	 * macro/templates.
	 */
	DT_TEST(const_reference_cast)
	{
		DT_ASSERT_SUCCESS({
			const Derived& constDerivedRef =
				dynamic_cast<const Derived&>(constBaseRef);
			success = (&constDerivedRef == &localDerived &&
				constDerivedRef.tag() == 16);
		})
		DT_ASSERT_CONDITION(success)
	}
	
	/*
	 * Verify that a bad reference cast still throws the std::bad_cast
	 * exception with the new macro/templates.
	 */
	DT_TEST(bad_reference_cast)
	{
		DT_ASSERT_THROWS(std::bad_cast,
		{
			Derived& derivedRef = dynamic_cast<Derived&>(localBase);
			derivedRef.tag();
		})
	}
	
	/*
	 * Verify that a non-lvalue can be passed into the new macro/templates.
	 */
	DT_TEST(direct_from_new_cast)
	{
		DT_ASSERT_SUCCESS({
			checked(Derived*) pDerived = dynamic_cast<Derived*>(new Derived(4));
			delete pDerived;
		})
	}

	/* tear down */
	delete pBase;
	delete pJustBase;

DT_END_SUITE()

#endif // DYNAMIC_CAST_TESTS_H
