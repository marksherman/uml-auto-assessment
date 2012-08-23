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

#include "test_suite.h"

using namespace std;

namespace Dereferee
{

// ---------------------------------------------------------------------------
test_suite::~test_suite()
{
}

// ---------------------------------------------------------------------------
void test_suite::print_result(const char* description,
		size_t& total_tests, size_t& passed_tests, bool result,
		const char* reason)
{
	if(result)
	{
		printf("    pass");
		passed_tests++;
	}
	else
	{
		printf(" !! FAIL");
	}
	
	total_tests++;
	
	printf(" %s", description);
	
	if(!result)
	{
		error_code error = test_oriented_listener::last_error();
		if(error == no_test_error)
		{
			warning_code warning = test_oriented_listener::last_warning();
			if(warning == no_test_warning)
				printf(" (reason: %s)", reason);
			else
				printf(" (reason: %s)", warning_messages[warning]);
		}
		else
			printf(" (reason: %s)", error_messages[error]);
	}

	printf("\n");
}

} // namespace Dereferee