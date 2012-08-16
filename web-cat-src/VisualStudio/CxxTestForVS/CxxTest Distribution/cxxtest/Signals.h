#ifndef __CXXTEST__SIGNALS_H
#define __CXXTEST__SIGNALS_H

#ifdef CXXTEST_TRAP_SIGNALS

#include <iostream>
#include <string>
#include <signal.h>
#include <setjmp.h>

#include "Appender.h"

#ifdef _MSC_VER
#	ifndef _WIN32_WINNT
#		define _WIN32_WINNT 0x400
#	endif
#	include <windows.h>
#	include <excpt.h>
#endif

// This file holds the declarations for the support
// features used for trapping and returning from signal-
// based failures.

namespace CxxTest
{
    extern std::string		__cxxtest_sigmsg;
    extern std::string		__cxxtest_assertmsg;
	extern bool				__cxxtest_runCompleted;

	extern void __append_backtrace_xml(void** backtrace, bool filter, Appender& appender);

	extern void doWarn(const char*, unsigned int, const char*);

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

	extern void __cxxtest_se_handler(__se_exception& e);

	//
	// _TS_PROPAGATE_SIGNAL is required to ensure that, for Windows, a
	// _TRY block inside a _TRY_WITH_SIGNAL_PROTECTION block does not
	// swallow the structured exception that represents the "signal" that
	// we're trying to catch. Code using these blocks together should be
	// structured as follows:
	//
	// _TS_TRY_WITH_SIGNAL_PROTECTION {
	//     _TRY {
	//         statements...
	//     }
	//     _TS_PROPAGATE_SIGNAL
	//     _TS_CATCH...
	// }
	// _TS_CATCH_SIGNAL...
	// 
	// On non-Win32 systems, the PROPAGATE directive is a no-op, since
	// signal protection there uses setjmp/longjmp and this propagation
	// is unnecessary.
	//

#	define _TS_TRY_WITH_SIGNAL_PROTECTION try
#	define _TS_SIGNAL_CLEANUP
#	define _TS_PROPAGATE_SIGNAL \
		catch(CxxTest::__se_exception& e) { throw e; }
#	define _TS_CATCH_SIGNAL( action ) \
		catch(CxxTest::__se_exception& e) { \
			CxxTest::__cxxtest_se_handler(e); \
			action; \
		}

#else // !_MSC_VER

	// Use POSIX signal handling on other operating systems.

	const int __cxxtest_jmpmax = 10;
    extern jmp_buf            __cxxtest_jmpbuf[__cxxtest_jmpmax];

#ifdef CXXTEST_TRACE_STACK
    extern unsigned int          __cxxtest_stackTopBuf[__cxxtest_jmpmax];
    extern unsigned int          __cxxtest_stackTop;
    extern bool                  __cxxtest_handlingOverflow;

#   define _TS_SAVE_TRACE_STACK \
    CxxTest::__cxxtest_stackTopBuf[CxxTest::__cxxtest_jmppos] = \
    CxxTest::__cxxtest_stackTop
#   define _TS_RESTORE_TRACE_STACK \
    CxxTest::__cxxtest_stackTop = \
    CxxTest::__cxxtest_stackTopBuf[CxxTest::__cxxtest_jmppos]; \
    CxxTest::__cxxtest_handlingOverflow = false

#else
#   define _TS_SAVE_TRACE_STACK
#   define _TS_RESTORE_TRACE_STACK
#endif

	extern volatile sig_atomic_t __cxxtest_jmppos;

#define _TS_TRY_WITH_SIGNAL_PROTECTION \
    if ( ++CxxTest::__cxxtest_jmppos >= CxxTest::__cxxtest_jmpmax ) { \
		std::cout << "Too many nested signal handler levels.\n"; \
		exit( 1 ); \
	} \
    _TS_SAVE_TRACE_STACK; \
    if ( !setjmp(CxxTest::__cxxtest_jmpbuf[CxxTest::__cxxtest_jmppos]) )

#define _TS_SIGNAL_CLEANUP \
    { CxxTest::__cxxtest_jmppos--; __cxxtest_sigmsg = ""; }

#define _TS_PROPAGATE_SIGNAL

#define _TS_CATCH_SIGNAL( action ) \
    else { _TS_RESTORE_TRACE_STACK; action; } _TS_SIGNAL_CLEANUP

#endif // _MSC_VER


//
// Utility definitions to clean up code elsewhere.
//

#define _TS_THROWS_NO_SIGNAL( msg, action ) \
	_TS_TRY_WITH_SIGNAL_PROTECTION \
		action \
	_TS_CATCH_SIGNAL( { tracker().failedTest( __FILE__, __LINE__, msg ); } )

#define TS_MESSAGE_FOR_SIGNAL( msg ) ( CxxTest::__cxxtest_sigmsg = msg )

}

#else // !CXXTEST_TRAP_SIGNALS

#define _TS_TRY_WITH_SIGNAL_PROTECTION
#define _TS_CATCH_SIGNAL( action )
#define _TS_SIGNAL_CLEANUP
#define _TS_PROPAGATE_SIGNAL
#define _TS_THROWS_NO_SIGNAL( msg, action ) action
#define TS_MESSAGE_FOR_SIGNAL( msg )

#endif // CXXTEST_TRAP_SIGNALS

#endif
