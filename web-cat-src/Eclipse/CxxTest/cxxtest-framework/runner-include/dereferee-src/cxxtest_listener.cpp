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

#include <cstdlib>
#include <cstdio>
#include <cstdarg>
#include <cstring>
#include <string>
#include <dereferee/listener.h>

#include <cxxtest/MemoryTrackingListener.h>
#include <cxxtest/SafeString.h>

// ===========================================================================
/**
 * The cxxtest_xml_listener class is an implementation of the
 * Dereferee::listener class that maps Dereferee errors to CxxTest test case
 * failures, and generates its summary output to a hidden XML file in
 * the process's working directory which can then be processed by an IDE
 * (such as Eclipse or Visual Studio) to display the feedback to the user.
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
 * Interface and implementation of the cxxtest_xml_listener class.
 */
namespace DerefereeSupport
{

class cxxtest_xml_listener : public Dereferee::listener
{
private:
	const Dereferee::usage_stats* usage_stats;
	FILE* memory_log;
	size_t max_leaks;
	Dereferee::platform* platform;

    void output_backtrace(void** bt, CxxTest::SafeString& str);
    
    CxxTest::SafeString escape(const char* str);

public:
	cxxtest_xml_listener(const Dereferee::option* options,
		Dereferee::platform* platform);

	~cxxtest_xml_listener();

	size_t maximum_leaks_to_report();

    void* get_allocation_user_info(
        const Dereferee::allocation_info& alloc_info);

	void begin_report(const Dereferee::usage_stats& stats);

	bool should_report_leak(const Dereferee::allocation_info& leak);

	void report_leak(const Dereferee::allocation_info& leak);

	void report_truncated(size_t reports_logged, size_t actual_leaks);

	void end_report();

	void error(Dereferee::error_code code, va_list args);

	void warning(Dereferee::warning_code code, va_list args);
};

// ------------------------------------------------------------------
cxxtest_xml_listener::cxxtest_xml_listener(
	const Dereferee::option* options, Dereferee::platform* platform)
{
	// Initialize defaults.
	memory_log = fopen(".dereferee.log", "w");

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
}

// ------------------------------------------------------------------
cxxtest_xml_listener::~cxxtest_xml_listener()
{
	fclose(memory_log);
}

// ------------------------------------------------------------------
void* cxxtest_xml_listener::get_allocation_user_info(
    const Dereferee::allocation_info& alloc_info)
{
    return (void*) CxxTest::MemoryTrackingListener::tagAction(
        CxxTest::MemoryTrackingListener::GET);
}

// ------------------------------------------------------------------
size_t cxxtest_xml_listener::maximum_leaks_to_report()
{
	return max_leaks;
}

// ------------------------------------------------------------------
void cxxtest_xml_listener::begin_report(const Dereferee::usage_stats& stats)
{
	usage_stats = &stats;

	fprintf(memory_log, "<?xml version='1.0'?>\n");
	fprintf(memory_log, "<dereferee actual-leak-count=\"%lu\">\n",
		(unsigned long)stats.leaks());
}

// ------------------------------------------------------------------
bool cxxtest_xml_listener::should_report_leak(
	const Dereferee::allocation_info& leak)
{
    unsigned int tag = (unsigned int) leak.user_info();    
    return (tag & 0x80000000) == 0;
}

// ------------------------------------------------------------------
void cxxtest_xml_listener::report_leak(
	const Dereferee::allocation_info& leak)
{
	fprintf(memory_log, "    <leak address=\"%p\"", leak.address());

	if(leak.is_array())
		fprintf(memory_log, " is-array=\"true\"");

	if(leak.type_name())
	{
		if(leak.is_array())
			fprintf(memory_log, " type=\"%s[]\"", leak.type_name());
		else
			fprintf(memory_log, " type=\"%s\"", leak.type_name());
	}

	fprintf(memory_log, " size=\"%lu\">\n",
		(unsigned long)leak.block_size());

    CxxTest::SafeString btStr;
    output_backtrace(leak.backtrace(), btStr);
    fputs(btStr.c_str(), memory_log);
    
	fprintf(memory_log, "    </leak>\n");
}

// ------------------------------------------------------------------
CxxTest::SafeString cxxtest_xml_listener::escape(const char* str)
{
    CxxTest::SafeString result;
    
    while (*str)
    {
        switch (*str)
        {
            case '<': result += "&lt;"; break;
            case '>': result += "&gt;"; break;
            case '"': result += "&quot;"; break;
            case '\'': result += "&apos;"; break;
            case '&': result += "&amp;"; break;
            default: result += *str;
        }

        str++;
    }
    
    return result;
}

// ------------------------------------------------------------------
void cxxtest_xml_listener::output_backtrace(void** bt, CxxTest::SafeString& str)
{
    if (bt)
    {
        char function[DEREFEREE_MAX_FUNCTION_LEN];
        char filename[DEREFEREE_MAX_FILENAME_LEN];
        int line_number;
        char line_num_buffer[32];

        while(*bt)
        {
            if(platform->get_backtrace_frame_info(*bt,
                function, filename, &line_number))
            {
                if(CxxTest::filter_backtrace_frame(function))
                {
                    str += "<stack-frame function=\"";
                    str += escape(function).c_str();
                    str += "\" ";

                    if(line_number)
                    {
                        str += "location=\"";
                        str += escape(filename).c_str();
                        str += ":";

                        snprintf(line_num_buffer, 32, "%d", line_number);
                        str += line_num_buffer;

                        str += "\" ";
                    }

                    str += "/>\n";

                    if (strcmp(function, "main") == 0 ||
                        strstr(function, "CxxTestMain") == function)
                    {
                        break;
                    }
                }
            }

            bt++;
        }
    }
}

// ------------------------------------------------------------------
void cxxtest_xml_listener::report_truncated(size_t /* reports_logged */,
										size_t /* actual_leaks */)
{
	// Do nothing.
}

// ------------------------------------------------------------------
void cxxtest_xml_listener::end_report()
{
	fprintf(memory_log, "    <summary "
		"total-bytes-allocated=\"%d\" max-bytes-in-use=\"%d\" "
		"calls-to-new=\"%d\" calls-to-array-new=\"%d\" "
		"calls-to-delete=\"%d\" calls-to-array-delete=\"%d\" "
		"calls-to-delete-null=\"%d\""
		"/>\n",
		(int)usage_stats->total_bytes_allocated(),
		(int)usage_stats->maximum_bytes_in_use(),
		(int)usage_stats->calls_to_new(),
		(int)usage_stats->calls_to_array_new(),
		(int)usage_stats->calls_to_delete(),
		(int)usage_stats->calls_to_array_delete(),
		(int)usage_stats->calls_to_array_delete_null());

	fprintf(memory_log, "</dereferee>\n");
}

// ------------------------------------------------------------------
void cxxtest_xml_listener::error(Dereferee::error_code code, va_list args)
{
	char text[513] = { 0 };
	vsnprintf(text, 512, error_messages[code], args);
    CxxTest::__cxxtest_assertmsg = text;

#ifdef __CYGWIN__
	// Can't use abort() here because it hard-kills the process on Windows,
	// rather than raising a signal that would be caught so execution could
	// continue with the next test case. Instead, cause an access violation
	// that will be caught by the structured exception handler.
	int* x = 0;
	*x = 0xBADBEEF;
#else
	abort();
#endif
}

// ------------------------------------------------------------------
void cxxtest_xml_listener::warning(Dereferee::warning_code code,
									 va_list args)
{
	CxxTest::SafeString str;

	char msg[512] = { 0 };

	if(code == Dereferee::warning_memory_boundary_corrupted)
	{
		Dereferee::memory_corruption_location loc =
			(Dereferee::memory_corruption_location)va_arg(args, int);

		sprintf(msg, warning_messages[code], corruption_messages[loc]);
	}
	else
	{
		vsprintf(msg, warning_messages[code], args);
	}

	str += msg;

	if(!CxxTest::__cxxtest_runCompleted)
	{
		CxxTest::doWarn("", 0, str.c_str());
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
	return new DerefereeSupport::cxxtest_xml_listener(options, platform);
}

void Dereferee::destroy_listener(Dereferee::listener* listener)
{
	delete listener;
}

// ===========================================================================
