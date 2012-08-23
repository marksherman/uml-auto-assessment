#ifndef __cxxtest_Signals_cpp__
#define __cxxtest_Signals_cpp__

#include <cxxtest/Signals.h>

namespace CxxTest
{
    //
    // Declared in Signals.h
    //
    bool __cxxtest_runCompleted = false;
    void** __cxxtest_sig_backtrace = NULL;
	bool __cxxtest_handlingOverflow = false;


    //
    // Declared in SuiteInitFailureTable.h
    //
    CXXTEST_EARLIEST_INIT(SuiteInitFailureTable __cxxtest_failed_init_suites);


    //
    // Various statics used to keep track of states and signal-related
    // messages. Defined even when CXXTEST_TRAP_SIGNALS is unset, because
    // external listeners may want to manipulate them.
    //
    CXXTEST_EARLIEST_INIT(SafeString __cxxtest_sigmsg);
    CXXTEST_EARLIEST_INIT(SafeString __cxxtest_assertmsg);


    // ----------------------------------------------------------
    /**
     * Called to determine if a backtrace entry should be printed or not.
     * CxxTest and Dereferee functions are filtered out.
     */
    bool filter_backtrace_frame(const char* function)
    {
        if (strstr(function, "CxxTest::") == function ||
            strstr(function, "TestDescription_") == function ||
            strstr(function, "Dereferee::") == function ||
            strstr(function, "DerefereeSupport::") == function)
            return false;
        else
            return true;
    }

};

#endif // __cxxtest_Signals_cpp__
