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

#ifndef DEREFEREE_TEST_SUITE_H
#define DEREFEREE_TEST_SUITE_H

#include <string>
#include <vector>
#include <csignal>
#include <csetjmp>
#include "test_oriented_listener.h"

#ifdef _MSC_VER
#	ifndef _WIN32_WINNT
#		define _WIN32_WINNT 0x400
#	endif
#	include <windows.h>
#	include <excpt.h>
#endif

// ===========================================================================
/**
 * The code in this file is pretty horrific and not really intended for public
 * consumption -- all I wanted was to roll my own quick and dirty set of unit-
 * testing macros that I could use in test cases without getting bogged down in
 * the boilerplate code for setting up the signal protection and such. The
 * syntax for these macros is shameful and not at all an example of good
 * coding or design.
 *
 * Like the sun, avoid staring directly at it for too long.
 */

namespace Dereferee
{

// ===========================================================================
class test_suite
{
public:
	// -----------------------------------------------------------------------
	virtual ~test_suite();

	// -----------------------------------------------------------------------
	virtual void run_tests(size_t& total_tests, size_t& passed_tests) = 0;
	
protected:
	// -----------------------------------------------------------------------
	void print_result(const char* description,
			size_t& total_tests, size_t& passed_tests,
			bool result, const char* reason);
};

// ===========================================================================

#ifdef _MSC_VER
	// On Windows, we wrap SEH exceptions as C++ exceptions (compile with
	// /EHa) and handle them that way instead of using signals, which aren't
	// implemented as well on Windows.
	
	class __se_exception
	{
	public:
		__se_exception(unsigned int code, EXCEPTION_POINTERS* info)
		{
			_code = code;
			_info = info;
		}
		
		unsigned int code() const { return _code; }
		EXCEPTION_POINTERS* info() const { return _info; }
	
	private:
		unsigned int _code;
		EXCEPTION_POINTERS* _info;
	};

	extern void __dereferee_se_handler(__se_exception& e);

	#define DT_TRY try

	#define DT_CATCH(action) \
		catch(Dereferee::__se_exception&) { \
			action; \
			if(!__pass) continue; \
		}
	
#else
	const int __dereferee_jmpmax = 10;
	extern sigjmp_buf __dereferee_jmpbuf[__dereferee_jmpmax];
	extern volatile sig_atomic_t __dereferee_jmppos;
	
	#define DT_TRY \
	    if(++Dereferee::__dereferee_jmppos >= Dereferee::__dereferee_jmpmax ) { \
	    	exit( 1 ); \
	    } \
	    if(!sigsetjmp(Dereferee::__dereferee_jmpbuf[Dereferee::__dereferee_jmppos], 1))

	#define DT_CATCH(action) \
	    else { action; } { Dereferee::__dereferee_jmppos--; if(!__pass) continue; }

#endif

} // namespace Dereferee


// ===========================================================================

#define DT_REASON_ERROR "expected an error or warning, but there was none"
#define DT_REASON_EXCEPTION "expected a C++ exception, but none was thrown"

// ===========================================================================
#define DT_BEGIN_SUITE(name_) \
	class name_ : public Dereferee::test_suite { \
	public: \
	virtual void run_tests(size_t& total_tests, size_t& passed_tests) { \
		bool __pass; const char* __reason; \
		printf("Running tests in " #name_ ":\n");


// ---------------------------------------------------------------------------
#define DT_TEST(description_) \
		__pass = true; __reason = NULL; \
		for(int __dummy = 0; __dummy < 1; \
			print_result(#description_, total_tests, \
					passed_tests, __pass, __reason), __dummy++)


// ---------------------------------------------------------------------------
#define DT_END_SUITE() \
	} \
	};


// ---------------------------------------------------------------------------
#define DT_ASSERT_SUCCESS(block) \
	DT_TRY { \
		test_oriented_listener::clear_errors(); \
		block \
	} \
	DT_CATCH( { \
		__reason = DT_REASON_ERROR; __pass = false; \
	} ); \
	if(test_oriented_listener::last_warning() == no_test_warning) { \
		__pass = true; \
	} \
	else { \
		__pass = false; continue; \
	}


// ---------------------------------------------------------------------------
#define DT_ASSERT_ERROR(error, block) \
	DT_TRY { \
		test_oriented_listener::clear_errors(); \
		block \
	} \
	DT_CATCH( { \
		__pass = (error == test_oriented_listener::last_error()); \
	} ) \
	if(test_oriented_listener::last_error() == no_test_error) { \
		__reason = DT_REASON_ERROR; __pass = false; continue; \
	}


// ---------------------------------------------------------------------------
#define DT_ASSERT_THROWS(exception, block) \
	DT_TRY { \
		__pass = false; \
		test_oriented_listener::clear_errors(); \
		try { \
			block \
		} catch(exception) { \
			__pass = true; continue; \
		} \
	} \
	DT_CATCH( { \
		__reason = DT_REASON_EXCEPTION; __pass = false; continue; \
	} ) \
	if(test_oriented_listener::last_error() == no_test_error) { \
		__reason = DT_REASON_ERROR; __pass = false; continue; \
	}


// ---------------------------------------------------------------------------
#define DT_ASSERT_WARNING(warning, block) \
	DT_TRY { \
		test_oriented_listener::clear_errors(); \
		block \
	} \
	DT_CATCH( { \
		__reason = DT_REASON_ERROR; __pass = false; \
	} ); \
	if(test_oriented_listener::last_warning() != warning) { \
		__pass = false; continue; \
	} \
	else { \
		__pass = true; \
	}


// ---------------------------------------------------------------------------
#define DT_ASSERT_CONDITION(condition) \
	DT_TRY { \
		test_oriented_listener::clear_errors(); \
		if(!(condition)) { \
			__reason = #condition " == false"; __pass = false; \
		} \
	} \
	DT_CATCH( { \
		__reason = DT_REASON_ERROR; __pass = false; \
	} );


#endif // DEREFEREE_TEST_SUITE_H
