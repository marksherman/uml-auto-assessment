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

#ifndef _CRT_SECURE_NO_DEPRECATE
#define _CRT_SECURE_NO_DEPRECATE
#endif

#undef _WIN32_WINNT
#define _WIN32_WINNT 0x0400

#include <cstdlib>
#include <cstdio>
#include <cstdarg>
#include <cstring>
#include <string>
#include <windows.h>
#include <dbghelp.h>
#include <crtdbg.h>
#include <dereferee/listener.h>

#define CXXTEST_TRAP_SIGNALS
#define CXXTEST_TRACE_STACK

#include <cxxtest/Appender.h>
#include <cxxtest/Signals.h>

// ===========================================================================
/**
 * The msvc_win32_listener class is an implementation of the
 * Dereferee::listener class that either sends its output to the current
 * debugger (if one is present) or to stdout/stderr (if the process is not
 * being debugged). This listener is intended for use on Windows systems
 * with Visual C++ 2005 or higher as the compiler in use.
 *
 * To affect runtime behavior, the following options can be used:
 *
 * - "max.leaks.to.report": if set, the integer value of this variable
 *   will be used to specify the maximum number of memory leaks that should be
 *   reported at the end of execution.
 */

// ===========================================================================
/*
 * Messages corresponding to the error codes used by Dereferee.
 */
static const char* error_messages[] =
{
	"Checked pointers cannot point to memory that wasn't allocated with new or new[]",
	"Assigned dead (never initialized) pointer to another pointer",
	"Assigned dead (already deleted) pointer to another pointer",
	"Assigned dead (out of bounds) pointer to another pointer",
	"Called delete instead of delete[] on array pointer",
	"Called delete[] instead of delete on non-array pointer",
	"Called delete on (never initialized) dead pointer",
	"Called delete[] on (never initialized) dead pointer",
	"Called delete on (already deleted or not dynamically allocated) dead pointer",
	"Called delete[] on (already deleted or not dynamically allocated) dead pointer",
	"Dereferenced (never initialized) dead pointer using operator->",
	"Dereferenced (never initialized) dead pointer using operator*",
	"Dereferenced (never initialized) dead pointer using operator[]",
	"Dereferenced (already deleted) dead pointer using operator->",
	"Dereferenced (already deleted) dead pointer using operator*",
	"Dereferenced (already deleted) dead pointer using operator[]",
	"Dereferenced (out of bounds) dead pointer using operator->",
	"Dereferenced (out of bounds) dead pointer using operator*",
	"Dereferenced (out of bounds) dead pointer using operator[]",
	"Dereferenced null pointer using operator->",
	"Dereferenced null pointer using operator*",
	"Dereferenced null pointer using operator[]",
	"Used (never initialized) dead pointer in an expression",
	"Used (already deleted) dead pointer in an expression",
	"Used (out of bounds) dead pointer in an expression",
	"Used (never initialized) dead pointer in a comparison",
	"Used (already deleted) dead pointer in a comparison",
	"Used (out of bounds) dead pointer in a comparison",
	"Used null pointer on only one side of an inequality comparison; if one side is null then the both sides must be null",
	"Both pointers being compared are alive but point into different memory blocks, so the comparison is undefined",
	"Used (never initialized) dead pointer in an arithmetic expression",
	"Used (already deleted) dead pointer in an arithmetic expression",
	"Used (out of bounds) dead pointer in an arithmetic expression",
	"Used null pointer in an arithmetic expression",
	"Used null pointer on only one side of a pointer subtraction expression; if one side is null then both sides must be null",
	"Both pointers being subtracted are alive but point into different memory blocks, so the distance between them is undefined",
	"Pointer arithmetic has moved a live pointer out of bounds",
	"Used operator[] on a pointer that does not point to an array",
	"Array index %d is out of bounds; valid indices are in the range [0..%lu]",
	"A previous operation has made this pointer invalid"
};

// ===========================================================================
/*
 * Messages corresponding to the warning codes used by Dereferee.
 */
static const char* warning_messages[] =
{
	"Memory leak caused by last live pointer to memory block going out of scope",
	"Memory leak caused by last live pointer to memory block being overwritten",
	"Memory %s allocated block was corrupted, likely due to invalid array indexing or pointer arithmetic"
};

// ===========================================================================
/*
 * Memory block corruption types.
 */
static const char* corruption_messages[] =
{
	"", "before", "after", "before and after"
};

// ===========================================================================
/**
 * Interface and implementation of the msvc_win32_listener class.
 */
namespace DerefereeSupport
{

class msvc_debugger_listener : public Dereferee::listener
{
private:
	const Dereferee::usage_stats* usage_stats;
	FILE* memory_log;
	size_t max_leaks;
	BOOL debugging;
	Dereferee::platform* platform;

public:
	msvc_debugger_listener(const Dereferee::option* options,
		Dereferee::platform* platform);
	
	~msvc_debugger_listener();

	size_t maximum_leaks_to_report();

	void begin_report(const Dereferee::usage_stats& stats);

	void report_leak(const Dereferee::allocation_info& leak);
	
	void report_truncated(size_t reports_logged, size_t actual_leaks);
	
	void end_report();
	
	void error(Dereferee::error_code code, va_list args);

	void warning(Dereferee::warning_code code, va_list args);
};

// ---------------------------------------------------------------------------
msvc_debugger_listener::msvc_debugger_listener(
	const Dereferee::option* options, Dereferee::platform* platform)
{
	// Initialize defaults.
	memory_log = fopen("_dereferee.log", "w");

	max_leaks = 100;
	this->platform = platform;

	while(options->key != NULL)
	{
		if(strcmp(options->key, "max.leaks.to.report") == 0)
		{
			max_leaks = atoi(options->value);
		}
		
		options++;
	}

	// Determine if a debugger is currently present over this process. If so,
	// we send any output that we generate to the debugger via
	// OutputDebugString instead of stdout or stderr.
	debugging = IsDebuggerPresent();
}

// ---------------------------------------------------------------------------
msvc_debugger_listener::~msvc_debugger_listener()
{
	fclose(memory_log);
}

// ------------------------------------------------------------------
size_t msvc_debugger_listener::maximum_leaks_to_report()
{
	return max_leaks;
}

// ------------------------------------------------------------------
void msvc_debugger_listener::begin_report(const Dereferee::usage_stats& stats)
{
	usage_stats = &stats;

	fprintf(memory_log, "<?xml version='1.0'?>\n");
	fprintf(memory_log, "<dereferee actual-leak-count=\"%lu\">\n",
		(unsigned long)stats.leaks());
}

// ------------------------------------------------------------------
void msvc_debugger_listener::report_leak(
	const Dereferee::allocation_info& leak)
{
	fprintf(memory_log, "    <leak address=\"%p\"", leak.address());

	if(leak.is_array())
		fprintf(memory_log, " is-array=\"true\"");

	if(leak.type_name())
	{
		if(leak.is_array())
			fprintf(memory_log, " type=\"%s[%lu]\"", leak.type_name(),
				(unsigned long)leak.array_size());
		else
			fprintf(memory_log, " type=\"%s\"", leak.type_name());
	}

	fprintf(memory_log, " size=\"%lu\">\n",
		(unsigned long)leak.block_size());

	CxxTest::FileAppender appender(memory_log);
	CxxTest::__append_backtrace_xml(leak.backtrace(), true, appender);

	fprintf(memory_log, "    </leak>\n");
}		

// ------------------------------------------------------------------
void msvc_debugger_listener::report_truncated(size_t /* reports_logged */,
											  size_t /* actual_leaks */)
{
	// Do nothing.
}

// ------------------------------------------------------------------
void msvc_debugger_listener::end_report()
{
	fprintf(memory_log, "    <summary "
		"total-bytes-allocated=\"%d\" max-bytes-in-use=\"%d\" "
		"calls-to-new=\"%d\" calls-to-array-new=\"%d\" "
		"calls-to-delete=\"%d\" calls-to-array-delete=\"%d\" "
		"calls-to-delete-null=\"%d\"" 
		"/>\n",
		usage_stats->total_bytes_allocated(),
		usage_stats->maximum_bytes_in_use(),
		usage_stats->calls_to_new(), usage_stats->calls_to_array_new(),
		usage_stats->calls_to_delete(), usage_stats->calls_to_array_delete(),
		usage_stats->calls_to_array_delete_null());

	fprintf(memory_log, "</dereferee>\n");
}

// ------------------------------------------------------------------
void msvc_debugger_listener::error(Dereferee::error_code code, va_list args)
{
	char text[513];
	vsprintf(text, error_messages[code], args);
	CxxTest::__cxxtest_assertmsg = text;

	if(debugging)
	{
		void** bt = platform->get_backtrace(NULL, NULL);
		int bufsize = vsnprintf(NULL, 0, error_messages[code], args) + 1;
		char* buffer = (char*)malloc(bufsize);
		vsnprintf(buffer, bufsize, error_messages[code], args);

		char function[DEREFEREE_MAX_FUNCTION_LEN];
		char filename[DEREFEREE_MAX_FILENAME_LEN];
		int line_number;

		void *addr = bt[4];

		bool success = platform->get_backtrace_frame_info(
			addr, function, filename, &line_number);

		platform->free_backtrace(bt);

#ifdef _DEBUG
		if(1 == _CrtDbgReport(_CRT_ERROR, filename,
			line_number, _pgmptr, buffer))
			_CrtDbgBreak();
#endif

		free(buffer);
	}

	// Can't use abort() here because it hard-kills the process on Windows,
	// rather than raising a signal that would be caught so execution could
	// continue with the next test case. Instead, cause an access violation
	// that will be caught by the structured exception handler.
	int* x = 0;
	*x = 0xBADBEEF;
}

// ------------------------------------------------------------------
void msvc_debugger_listener::warning(Dereferee::warning_code code,
									 va_list args)
{
	CxxTest::StringAppender appender;

	char msg[512];
	vsprintf(msg, warning_messages[code], args);

	appender.append_str(msg);
	appender.append('\n');

	void** bt = platform->get_backtrace(NULL, NULL);

	CxxTest::__append_backtrace_xml(bt, true, appender);
	platform->free_backtrace(bt);

	if(!CxxTest::__cxxtest_runCompleted)
	{
		CxxTest::doWarn("", 0, appender.str());
	}
}

} // end namespace DerefereeSupport

// ===========================================================================
/*
 * Implementation of the functions called by the Dereferee memory manager to
 * create and destroy the listener object.
 */

Dereferee::listener* Dereferee::create_listener(
	const Dereferee::option* options, Dereferee::platform* platform)
{
	return new DerefereeSupport::msvc_debugger_listener(options, platform);
}

void Dereferee::destroy_listener(Dereferee::listener* listener)
{
	delete listener;
}

// ===========================================================================
