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

#ifndef DEREFEREE_TEST_RUNNER_H
#define DEREFEREE_TEST_RUNNER_H

#include <vector>
#include "test_suite.h"

namespace Dereferee
{

class test_runner
{
private:
	typedef std::vector<test_suite*> test_suite_vector;
	test_suite_vector suites;

	void install_signal_handler();

public:
	~test_runner();
	
	void add_suite(test_suite* suite);

	void run();
};

}

#endif // DEREFEREE_TEST_RUNNER_H
