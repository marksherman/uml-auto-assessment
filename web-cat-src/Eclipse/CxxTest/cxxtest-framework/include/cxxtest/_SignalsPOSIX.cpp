#ifndef __cxxtest___SignalsPOSIX_cpp__
#define __cxxtest___SignalsPOSIX_cpp__

#ifdef CXXTEST_TRAP_SIGNALS

// Root signal and stack trace definitions for POSIX-based systems.
// Included by Root.cpp.

namespace CxxTest
{
    //
    // For POSIX-based platforms, trap runtime crashes with a POSIX signal
    // handler.
    //

    void __cxxtest_sig_handler( int, siginfo_t*, void* ) _CXXTEST_NO_INSTR;


    //
    // setjmp/longjmp bookkeeping for non-MSVC++ platforms.
    //
    sigjmp_buf __cxxtest_jmpbuf[__cxxtest_jmpmax];
    volatile sig_atomic_t __cxxtest_jmppos = -1;
	int __cxxtest_last_signal = 0;
	bool __cxxtest_last_abort_was_overflow = false;


    // ----------------------------------------------------------
    /**
     * Early "signal registration" object.
     */
    class SignalRegistrar
    {
    public:
        // ------------------------------------------------------
        SignalRegistrar()
        {
            struct sigaction act;
            act.sa_sigaction = __cxxtest_sig_handler;
            act.sa_flags = SA_SIGINFO;
            sigaction( SIGSEGV, &act, 0 );
            sigaction( SIGFPE,  &act, 0 );
            sigaction( SIGILL,  &act, 0 );
            sigaction( SIGBUS,  &act, 0 );
            sigaction( SIGABRT, &act, 0 );
            sigaction( SIGTRAP, &act, 0 );
    #ifdef SIGEMT
            sigaction( SIGEMT,  &act, 0 );
    #endif
            sigaction( SIGSYS,  &act, 0 );
            sigaction( SIGALRM, &act, 0 );
        }
    };
    CXXTEST_EARLIEST_INIT(SignalRegistrar __signal_registrar);


    // ----------------------------------------------------------
    void __cxxtest_sig_handler( int signum, siginfo_t* /* info */,
                                void* /* arg */ )
    {
		__cxxtest_last_signal = signum;
		CxxTest::__cxxtest_last_abort_was_overflow = false;

        const char* msg = "run-time exception";

        switch ( signum )
        {
            case SIGFPE:
                msg = "SIGFPE: floating point exception (division by zero?)";
                // Currently, can't get cygwin g++ to pass in info,
                // so we can't be more specific.
                break;

            case SIGSEGV:
                msg = "SIGSEGV: segmentation fault "
                    "(null or invalid pointer dereference?)";
                break;

            case SIGILL:
                msg = "SIGILL: illegal instruction "
                    "(dereference uninitialized or deleted pointer?)";
                break;

            case SIGABRT:
                msg = "SIGABRT: execution aborted "
                    "(failed assertion, corrupted heap, or other problem?)";
                break;

    #ifdef SIGEMT
            case SIGEMT:
                msg = "SIGEMT: EMT instruction";
                break;
    #endif

            case SIGTRAP:
                msg = "SIGTRAP: trace trap";
                break;

            case SIGBUS:
                msg = "SIGBUS: bus error "
                    "(dereference uninitialized or deleted pointer?)";
                break;

            case SIGSYS:
                msg = "SIGSYS: bad argument to system call";
                break;

            case SIGALRM:
                msg = "SIGALRM: allotted time expired";
                break;
        }

		if (CxxTest::__cxxtest_handlingOverflow)
			CxxTest::__cxxtest_last_abort_was_overflow = true;

        if ( !CxxTest::__cxxtest_assertmsg.empty() )
        {
            CxxTest::__cxxtest_sigmsg = CxxTest::__cxxtest_assertmsg;
            CxxTest::__cxxtest_assertmsg = "";
        }
        else if ( CxxTest::__cxxtest_sigmsg.empty() )
        {
            CxxTest::__cxxtest_sigmsg = msg;
        }
        else
        {
            SafeString oldsigmsg = CxxTest::__cxxtest_sigmsg;
            
            CxxTest::__cxxtest_sigmsg = msg;
            CxxTest::__cxxtest_sigmsg += ", maybe related to ";
            CxxTest::__cxxtest_sigmsg += oldsigmsg;
        }

#ifdef CXXTEST_TRACE_STACK

        Dereferee::platform* platform = Dereferee::current_platform();
        CxxTest::__cxxtest_sig_backtrace = platform->get_backtrace(NULL, NULL);

#endif // CXXTEST_TRACE_STACK

        if ( CxxTest::__cxxtest_jmppos >= 0 )
        {
            siglongjmp( CxxTest::__cxxtest_jmpbuf[CxxTest::__cxxtest_jmppos], 1 );
        }
        else
        {
            puts("\nError: untrapped signal:\n");
            puts(CxxTest::__cxxtest_sigmsg.c_str());
            puts("\n");

            exit(1);
        }
    }
} // end namespace CxxTest


#include <signal.h>
#include <sstream>

#ifndef HINT_PREFIX
#define HINT_PREFIX ""
#endif

extern "C" {

#ifdef __CYGWIN__
    void __assert( const char* file, int line, const char* failedexpr )
#else
    void __eprintf( const char* /* fmt */, const char* file, unsigned line,
                    const char* failedexpr )
#endif // __CYGWIN__
    {
        CxxTest::SafeString str;
        str += HINT_PREFIX;
        str += "assertion \"";
        str += failedexpr;
        str += "\" failed: file \"";
        str += file;
        str += "\", line ";
        
#define FMTBUFFERLEN 32
        char fmtBuffer[FMTBUFFERLEN];
        snprintf(fmtBuffer, FMTBUFFERLEN, "%u", line);
        str += fmtBuffer;

        CxxTest::__cxxtest_assertmsg = str;
        abort();
    }

#ifdef __THROW
#define _CXXTEST_ABORT_THROW __THROW
#else
#define _CXXTEST_ABORT_THROW
#endif

    void abort() _CXXTEST_ABORT_THROW _CXXTEST_NO_INSTR;
    void abort() _CXXTEST_ABORT_THROW
    {
#ifndef __CYGWIN__
        raise( SIGABRT );
#else
    const int INTENTIONAL_NULL_POINTER_DEREFERENCE = 0xBADBEEF;
        // gdb under cygwin does not suspend on software-raised signals,
        // so here we will force a true segfault so that the user can
        // spot the cause in a debugger.  Messages via CxxTest's signal
        // handler will be the same as if SIGABRT were used, so there
        // shouldn't be any visible difference in behavior.
        int* foo = 0;
        *foo = INTENTIONAL_NULL_POINTER_DEREFERENCE;
        // LOOK: two levels up in the debugger stack trace for the source of
        // the problem
#endif // __CYGWIN__
        exit( 1 );
    }

} // end extern "C"

#endif // CXXTEST_TRAP_SIGNALS

#endif // __cxxtest___SignalsPOSIX_cpp__
