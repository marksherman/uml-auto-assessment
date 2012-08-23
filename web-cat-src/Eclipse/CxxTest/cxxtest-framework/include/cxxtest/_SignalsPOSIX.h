#ifndef __cxxtest___SignalsPOSIX_h__
#define __cxxtest___SignalsPOSIX_h__

//
// Use POSIX signal handling on POSIX-based operating systems.
//

#include <signal.h>
#include <setjmp.h>
#include <cstdlib>

namespace CxxTest
{
    const int                       __cxxtest_jmpmax = 10;
    extern sigjmp_buf                  __cxxtest_jmpbuf[__cxxtest_jmpmax];
    extern volatile sig_atomic_t    __cxxtest_jmppos;
	extern int                      __cxxtest_last_signal;
	extern bool                     __cxxtest_last_abort_was_overflow;

    // ----------------------------------------------------------
#ifdef CXXTEST_TRACE_STACK

    #define _TS_FREE_BACKTRACE \
        Dereferee::current_platform()->free_backtrace( \
            CxxTest::__cxxtest_sig_backtrace)

    #define _TS_SAVE_BT_CONTEXT \
        Dereferee::current_platform()->save_current_context();

    #define _TS_RESTORE_BT_CONTEXT \
        Dereferee::current_platform()->restore_current_context();

#else // !CXXTEST_TRACE_STACK

    #define _TS_FREE_BACKTRACE
    #define _TS_SAVE_BT_CONTEXT
    #define _TS_RESTORE_BT_CONTEXT

#endif


    // ----------------------------------------------------------
    #define _TS_TRY_WITH_SIGNAL_PROTECTION \
        CxxTest::__cxxtest_sig_backtrace = NULL; \
        if ( ++CxxTest::__cxxtest_jmppos >= CxxTest::__cxxtest_jmpmax ) { \
            puts("Too many nested signal handler levels.\n"); \
            exit( 1 ); \
        } \
        _TS_SAVE_BT_CONTEXT; \
        if ( !sigsetjmp(CxxTest::__cxxtest_jmpbuf[CxxTest::__cxxtest_jmppos], 1) )


    // ----------------------------------------------------------
    #define _TS_SIGNAL_CLEANUP { \
            _TS_FREE_BACKTRACE; \
            CxxTest::__cxxtest_jmppos--; \
            CxxTest::__cxxtest_sigmsg = ""; \
        }


    // ----------------------------------------------------------
    #define _TS_PROPAGATE_SIGNAL


    // ----------------------------------------------------------
    #define _TS_CATCH_SIGNAL( action ) \
        else { action; } _TS_RESTORE_BT_CONTEXT; _TS_SIGNAL_CLEANUP


} // end namespace CxxTest


#endif // __cxxtest___SignalsPOSIX_h__
