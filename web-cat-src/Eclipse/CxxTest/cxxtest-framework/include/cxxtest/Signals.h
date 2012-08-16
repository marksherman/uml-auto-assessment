#ifndef __cxxtest_Signals_h__
#define __cxxtest_Signals_h__

//
// This file holds the declarations for the support
// features used for trapping and returning from signal-
// based failures.
//

#include <cxxtest/SafeString.h>
#include <dereferee.h>

namespace CxxTest
{
    //
    // Defined in Root.cpp
    //
    extern bool         __cxxtest_runCompleted;
    extern void**       __cxxtest_sig_backtrace;
	extern bool __cxxtest_handlingOverflow;
}


#ifdef CXXTEST_TRAP_SIGNALS

//
// Keep this file a little cleaner by factoring out platform-specific
// definitions into their own files.
//

#ifdef _MSC_VER
#   include <cxxtest/_SignalsWin32.h>
#else
#   include <cxxtest/_SignalsPOSIX.h>
#endif


namespace CxxTest
{
    //
    // Defined in Root.cpp
    //
    extern SafeString   __cxxtest_sigmsg;
    extern SafeString   __cxxtest_assertmsg;

    // ----------------------------------------------------------
    #define _TS_THROWS_NO_SIGNAL( msg, action ) \
        _TS_TRY_WITH_SIGNAL_PROTECTION \
            action \
        _TS_CATCH_SIGNAL( { tracker().failedTest( __FILE__, __LINE__, msg ); } )


    // ----------------------------------------------------------
    #define TS_MESSAGE_FOR_SIGNAL( msg ) \
        ( CxxTest::__cxxtest_sigmsg = msg )


} // end namespace CxxTest

#else // !CXXTEST_TRAP_SIGNALS

    #define _TS_TRY_WITH_SIGNAL_PROTECTION
    #define _TS_CATCH_SIGNAL( action )
    #define _TS_SIGNAL_CLEANUP
    #define _TS_PROPAGATE_SIGNAL
    #define _TS_THROWS_NO_SIGNAL( msg, action ) action
    #define TS_MESSAGE_FOR_SIGNAL( msg )

#endif // CXXTEST_TRAP_SIGNALS

#endif // __cxxtest_Signals_h__
