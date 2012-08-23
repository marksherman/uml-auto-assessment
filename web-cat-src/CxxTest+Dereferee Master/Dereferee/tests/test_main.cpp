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

#include "test_runner.h"
#include "all_tests.h"

#include <cstdio>
#include <memory>
#include <vector>
#include <string>

using namespace std;

int main(int /*argc*/, char** /*argv*/)
{
	Dereferee::test_runner runner;
	runner.add_suite(new initialization_assignment_tests);
	runner.add_suite(new comparison_tests);
	runner.add_suite(new const_correctness_tests);
	runner.add_suite(new array_indexing_tests);
	runner.add_suite(new memory_leak_tests);
	runner.add_suite(new polymorphism_tests);
	runner.add_suite(new new_operator_tests);
	runner.add_suite(new delete_operator_tests);
	runner.add_suite(new dynamic_cast_tests);
	runner.add_suite(new const_cast_tests);
	runner.add_suite(new arithmetic_tests);
	runner.add_suite(new no_default_ctor_tests);
	runner.add_suite(new abstract_class_tests);

	runner.run();

	return 0;
}
