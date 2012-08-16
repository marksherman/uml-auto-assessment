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

#ifndef __TEST_ORIENTED_LISTENER_H__
#define __TEST_ORIENTED_LISTENER_H__

#include <cstdlib>
#include <cstdarg>
#include <dereferee/listener.h>
#include <dereferee/platform.h>

// ===========================================================================
/**
 * The test_oriented_listener class is an implementation of the
 * Dereferee::listener class that does not generate any output. Rather, it
 * stores the error code of the most recent error so that in can be asserted
 * in a test case.
 */
class test_oriented_listener : public Dereferee::listener
{
private:
	static Dereferee::error_code _last_error;

	static Dereferee::warning_code _last_warning;

public:
	// -----------------------------------------------------------------------
	test_oriented_listener();
	
	// -----------------------------------------------------------------------
	~test_oriented_listener();

	// -----------------------------------------------------------------------
	size_t maximum_leaks_to_report();

	// -----------------------------------------------------------------------
	void begin_report(const Dereferee::usage_stats& stats);

	// -----------------------------------------------------------------------
	void report_leak(const Dereferee::allocation_info& leak);
	
	// -----------------------------------------------------------------------
	void report_truncated(size_t reports_logged,
			size_t actual_leaks);
	
	// -----------------------------------------------------------------------
	void end_report();
	
	// -----------------------------------------------------------------------
	void error(Dereferee::error_code code, va_list args);

	// -----------------------------------------------------------------------
	void warning(Dereferee::warning_code code, va_list args);
	
	// -----------------------------------------------------------------------
	static void clear_errors();
	
	// -----------------------------------------------------------------------
	static Dereferee::error_code last_error();

	// -----------------------------------------------------------------------
	static Dereferee::warning_code last_warning();
};

const Dereferee::error_code no_test_error = (Dereferee::error_code)-1;
const Dereferee::warning_code no_test_warning = (Dereferee::warning_code)-1;

extern const char* error_messages[];
extern const char* warning_messages[];

#endif // __TEST_ORIENTED_LISTENER_H__
