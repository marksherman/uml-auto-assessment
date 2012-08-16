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

#include <cstdio>
#include <csignal>
#include <csetjmp>

#ifdef _MSC_VER
#	define PRINTF_SIZE_T "%lu"
#else
#	define PRINTF_SIZE_T "%zu"
#endif

using namespace std;

extern "C"
{
#ifdef _MSC_VER
	namespace Dereferee
	{
		void __dereferee_se_translator(unsigned int, EXCEPTION_POINTERS*);
	}
#else
	void __dereferee_signal_handler(int, siginfo_t*, void*);
#endif
}

namespace Dereferee
{

test_runner::~test_runner()
{
	for(test_suite_vector::iterator suite_it = suites.begin();
		suite_it != suites.end(); ++suite_it)
	{
		test_suite* suite = *suite_it;
		delete suite;
	}
}

void test_runner::install_signal_handler()
{
#ifdef _MSC_VER
	_set_se_translator(Dereferee::__dereferee_se_translator);
#else
	struct sigaction act;
	act.sa_sigaction = __dereferee_signal_handler;
	act.sa_flags = SA_SIGINFO;
	sigaction(SIGABRT, &act, 0);
#endif
}

void test_runner::add_suite(test_suite* suite)
{
	suites.push_back(suite);
}
	
void test_runner::run()
{
	install_signal_handler();

	size_t total_tests = 0;
	size_t passed_tests = 0;

	for(test_suite_vector::iterator suite_it = suites.begin();
		suite_it != suites.end(); ++suite_it)
	{
		test_suite* suite = *suite_it;
		suite->run_tests(total_tests, passed_tests);
	}

	printf("\nPassed " PRINTF_SIZE_T " out of " PRINTF_SIZE_T
		" tests (%d%%).\n", passed_tests, total_tests,
		(int)(((float)passed_tests / total_tests) * 100));
}

#ifndef _MSC_VER
	sigjmp_buf __dereferee_jmpbuf[__dereferee_jmpmax];
	volatile sig_atomic_t __dereferee_jmppos = -1;
#endif

} // namespace Dereferee


// ============================================================================
/**
 * Under Cygwin, the abort() function doesn't seem to send a proper signal --
 * instead, the program simply exits. In this scenario we redefine abort() so
 * that it explicitly raises SIGABRT to ensure that the signal handler is
 * invoked.
 */
#ifndef _MSC_VER

#ifdef __THROW
#	define DEREFEREE_ABORT_THROW __THROW
#else
#	define DEREFEREE_ABORT_THROW
#endif

void abort() DEREFEREE_ABORT_THROW;

void abort() DEREFEREE_ABORT_THROW
{
	raise(SIGABRT);
	exit(1);
}

#endif // _MSC_VER

// ============================================================================
/**
 * This signal handler will return control to the DT_CATCH block that follows
 * a DT_TRY block within which a signal was raised. If a signal is raised
 * outside of a DT_TRY block, the program is exited.
 */
#ifdef _MSC_VER
namespace Dereferee
{

void __dereferee_se_translator(unsigned int code, EXCEPTION_POINTERS* info)
{
	throw Dereferee::__se_exception(code, info);
}

}
#else
void __dereferee_signal_handler(int signum, siginfo_t*, void*)
{
    if(Dereferee::__dereferee_jmppos >= 0)
    {
		siglongjmp(
			Dereferee::__dereferee_jmpbuf[Dereferee::__dereferee_jmppos],
        	1);
    }
    else
    {
    	printf("untrapped signal %d\n", signum);
    	exit(1);
    }
}
#endif
