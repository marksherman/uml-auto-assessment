#ifndef __CXXTEST__ROOT_CPP
#define __CXXTEST__ROOT_CPP

//
// This file holds the "root" of CxxTest, i.e.
// the parts that must be in a source file file.
//

#include <cxxtest/TestTracker.h>
#include <cxxtest/RealDescriptions.h>
#include <cxxtest/GlobalFixture.h>
#include <cxxtest/ValueTraits.h>
#include <cxxtest/Signals.h>
#include <cxxtest/Appender.h>
#include <dereferee.h>
#include <map>

namespace CxxTest
{
    //
    // Class statics
    //
    bool TestTracker::_created = false;
    List GlobalFixture::_list = { 0, 0 };
    List RealSuiteDescription::_suites = { 0, 0 };

#ifdef CXXTEST_TRAP_SIGNALS

	//
	// setjmp/longjmp bookkeeping for non-MSVC++ platforms.
	//
#	ifndef _MSC_VER
    jmp_buf __cxxtest_jmpbuf[__cxxtest_jmpmax];
#		ifdef CXXTEST_TRACE_STACK
    unsigned int __cxxtest_stackTopBuf[__cxxtest_jmpmax];
#		endif
    volatile sig_atomic_t __cxxtest_jmppos = -1;
#	endif

	//
	// Various statics used to keep track of states and signal-related
	// messages.
	//
	CXXTEST_EARLIEST_INIT(bool __cxxtest_runCompleted) = false;
    CXXTEST_EARLIEST_INIT(std::string __cxxtest_sigmsg);
	CXXTEST_EARLIEST_INIT(std::string __cxxtest_assertmsg);

	typedef std::map<std::string, std::string> SuiteInitFailureMap;
    CXXTEST_EARLIEST_INIT(SuiteInitFailureMap __cxxtest_failed_init_suites);



#	ifdef CXXTEST_TRACE_STACK

	CXXTEST_EARLIEST_INIT(static Dereferee::platform* __current_platform);

	//
	// Backtrace utility functions.
	//
	bool __filter_backtrace_frame(const char* function)
	{
		if(strstr(function, "CxxTest::") == function ||
			strstr(function, "TestDescription_") == function ||
			strstr(function, "Dereferee::") == function ||
			strstr(function, "DerefereeSupport::") == function)
			return false;
		else
			return true;
	}

	void __append_escaped_xml(const char* str, Appender& appender)
	{
		while(*str != 0)
		{
			char ch = *str++;

			switch(ch)
			{
				case '"':  appender.append_str("&quot;"); break;
				case '\'': appender.append_str("&apos;"); break;
				case '&':  appender.append_str("&amp;"); break;
				case '<':  appender.append_str("&lt;"); break;
				case '>':  appender.append_str("&gt;"); break;
				default:   appender.append(ch); break;
			}
		}
	}

	void __append_backtrace_xml(void** backtrace, bool filter, Appender& appender)
	{
		if(__current_platform == NULL)
		{
			Dereferee::option nullOption = { NULL, NULL };
			__current_platform = Dereferee::create_platform(&nullOption);
		}

		if(backtrace != NULL)
		{
			void **bt = backtrace;

			char function[DEREFEREE_MAX_FUNCTION_LEN];
			char filename[DEREFEREE_MAX_FILENAME_LEN];
			int line_number;

			while(*bt)
			{
				if(__current_platform->get_backtrace_frame_info(*bt,
					function, filename, &line_number))
				{
					if(!filter || (filter && __filter_backtrace_frame(function)))
					{
						appender.append_str("<stack-frame function=\"");
						__append_escaped_xml(function, appender);
						appender.append_str("\" ");

						if(line_number)
						{
							appender.append_str("location=\"");
							__append_escaped_xml(filename, appender);
							appender.append_str(":");
							appender.append_int(line_number);
							appender.append_str("\" ");
						}

						appender.append_str("/>\n");

						if(strcmp(function, "main") == 0 ||
							strstr(function, "CxxTestMain") == function)
							break;
					}
				}
				else if(!filter)
				{
					char addr_str[128];
					sprintf(addr_str, "%p", *bt);

					appender.append_str("<stack-frame function=\"");
					appender.append_str(addr_str);
					appender.append_str("\" />\n");
				}
				
				bt++;
			}
		}
	}

#	else // !CXXTEST_TRACE_STACK

	void __append_escaped_xml(const char* str, Appender& appender) { }
	void __append_backtrace_xml(void** backtrace, bool filter, Appender& appender) { }

#	endif // CXXTEST_TRACE_STACK


	//
	// Utility functions used to keep track of suite initialization errors.
	//

	void addSuiteToFailures(const char *name, const char *reason)
	{
		CxxTest::__cxxtest_failed_init_suites[std::string(name)] = std::string(reason);
	}

	bool didSuiteFailInitialization(const char* name, std::string& reason)
	{
		SuiteInitFailureMap::const_iterator it = __cxxtest_failed_init_suites.find(std::string(name));
		if(it != __cxxtest_failed_init_suites.end())
		{
			reason = it->second;
			return true;
		}
		
		return false;
	}


#	ifdef _MSC_VER

	//
	// Microsoft Visual C++ uses a Win32 structured exception -> C++ exception
	// translator to capture runtime crashes, instead of POSIX signals (which
	// are very poorly implemented on Windows).
	//

	// 
	// Translator function to convert Win32 structured exceptions into C++
	// exceptions.
	//
	void __cxxtest_se_translator( unsigned int code, EXCEPTION_POINTERS* info )
	{
		throw __se_exception(code, info);
	}

	//
	// Early "signal registration" object.
	//
	class SignalRegistrar
	{
	public:
		SignalRegistrar()
		{
			_set_se_translator(CxxTest::__cxxtest_se_translator);
		}
	};
	CXXTEST_EARLIEST_INIT(SignalRegistrar __signal_registrar);

	//
	// Handler to translate structured exception codes into meaningful
	// messages for the user, and to obtain a backtrace.
	//
	void __cxxtest_se_handler(__se_exception& e)
	{
		const char* msg = "Run-time exception";

		switch(e.code())
		{
			case EXCEPTION_ACCESS_VIOLATION:
				msg = "Access violation (null or invalid pointer dereference?)";
				break;

			case EXCEPTION_FLT_DIVIDE_BY_ZERO:
				msg = "Floating-point division by zero";
				break;

			case EXCEPTION_FLT_OVERFLOW:
				msg = "Floating-point overflow";
				break;

			case EXCEPTION_FLT_UNDERFLOW:
				msg = "Floating-point underflow";
				break;

			case EXCEPTION_ILLEGAL_INSTRUCTION:
				msg = "Illegal instruction";
				break;

			case EXCEPTION_INT_DIVIDE_BY_ZERO:
				msg = "Integer division by zero";
				break;

			case EXCEPTION_STACK_OVERFLOW:
				msg = "Stack overflow (infinite recursion, or allocation of a very large local variable?)";
				break;
		}

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
			CxxTest::__cxxtest_sigmsg = std::string(msg)
				+ ", maybe related to " + CxxTest::__cxxtest_sigmsg;
		}

#	ifdef CXXTEST_TRACE_STACK
		void* eip = (void*)e.info()->ContextRecord->Eip;
		void* ebp = (void*)e.info()->ContextRecord->Ebp;

		if(__current_platform == NULL)
		{
			Dereferee::option nullOption = { NULL, NULL };
			__current_platform = Dereferee::create_platform(&nullOption);
		}

		void **backtrace = __current_platform->get_backtrace(eip, ebp);

		StringAppender appender;
		__append_backtrace_xml(backtrace, true, appender);

		CxxTest::__cxxtest_sigmsg += "\n";
		CxxTest::__cxxtest_sigmsg += appender.str();

		__current_platform->free_backtrace(backtrace);
#	endif // CXXTEST_TRACE_STACK
	}

#	else // !_MSC_VER

	//
	// For other platforms, trap runtime crashes with a POSIX signal handler.
	//

	void __cxxtest_sig_handler( int, siginfo_t*, void* ) _CXXTEST_NO_INSTR;

	class SignalRegistrar
	{
	public:
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

	void __cxxtest_sig_handler( int signum, siginfo_t* /* info */, void* /* arg */ )
	{
		const char* msg = "run-time exception";

		switch ( signum )
		{
			case SIGFPE:
				msg = "SIGFPE: floating point exception (division by zero?)";
				// Currently, can't get cygwin g++ to pass in info,
				// so we can't be more specific.
				break;

			case SIGSEGV:
				msg = "SIGSEGV: segmentation fault (null or invalid pointer dereference?)";
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
			CxxTest::__cxxtest_sigmsg = std::string(msg)
				+ ", maybe related to " + CxxTest::__cxxtest_sigmsg;
		}

#	ifdef CXXTEST_TRACE_STACK
		if(__current_platform == NULL)
		{
			Dereferee::option nullOption = { NULL, NULL };
			__current_platform = Dereferee::create_platform(&nullOption);
		}

		void **backtrace = __current_platform->get_backtrace(NULL, NULL);

		StringAppender appender;
		__append_backtrace_xml(backtrace, true, appender);

		CxxTest::__cxxtest_sigmsg += "\n";
		CxxTest::__cxxtest_sigmsg += appender.str();

		__current_platform->free_backtrace(backtrace);
#	endif // CXXTEST_TRACE_STACK

		if ( CxxTest::__cxxtest_jmppos \>= 0 )
		{
			signal(SIGSEGV, __cxxtest_sig_handler);
			longjmp( CxxTest::__cxxtest_jmpbuf[CxxTest::__cxxtest_jmppos], 1 );
		}
		else
		{
			std::cout \<\< "\nError: untrapped signal:\n"
				\<\< CxxTest::__cxxtest_sigmsg
				\<\< "\n"; // std::endl;
			exit(1);
		}
	}

#	endif // _MSC_VER

#else // !CXXTEST_TRAP_SIGNALS

	void addSuiteToFailures(const std::string& name, const std::string& reason)
	{
	}

	bool didSuiteFailInitialization(const std::string& name, std::string& reason)
	{
		return false;
	}

#endif // CXXTEST_TRAP_SIGNALS

    //
    // Some compilers get confused by these functions if they're inline
    //
    bool RealTestDescription::setUp()
    {
        if ( !suite() )
            return false;

        bool ok = true;

		for ( GlobalFixture *gf = GlobalFixture::firstGlobalFixture();
			gf != 0; gf = gf->nextGlobalFixture() )
		{
			_TS_TRY_WITH_SIGNAL_PROTECTION
			{
				_TS_TRY
				{
					ok = gf->setUp();
				}
				_TS_PROPAGATE_SIGNAL
				_TS_LAST_CATCH( {
					tracker().failedTest( file(), line(),
						"Exception thrown from GlobalFixture::setUp()" );
					ok = false;
				} );
			}
			_TS_CATCH_SIGNAL({
				tracker().failedTest( file(), line(),
					__cxxtest_sigmsg.c_str() );
				ok = false;
			});

			if ( !ok ) {
				return false;
			}
		}

		_TS_TRY_WITH_SIGNAL_PROTECTION
		{
			_TS_TRY
			{
				_TSM_ASSERT_THROWS_NOTHING( file(), line(),
					"Exception thrown from setUp()",
					suite()->setUp() );
			}
			_TS_PROPAGATE_SIGNAL
			_TS_CATCH_ABORT( { ok = false; } );
		}
		_TS_CATCH_SIGNAL({
			tracker().failedTest( file(), line(), __cxxtest_sigmsg.c_str() );
			ok = false;
		});

		return ok;
	}

	bool RealTestDescription::tearDown()
	{
		if ( !suite() )
			return false;

		bool ok = true;

		_TS_TRY_WITH_SIGNAL_PROTECTION
		{
			_TS_TRY
			{
				_TSM_ASSERT_THROWS_NOTHING( file(), line(),
					"Exception thrown from tearDown()",
					suite()->tearDown() );
			}
			_TS_PROPAGATE_SIGNAL
			_TS_CATCH_ABORT( { ok = false; } );
		}
		_TS_CATCH_SIGNAL({
			tracker().failedTest( file(), line(),
				__cxxtest_sigmsg.c_str() );
			ok = false;
		});

		if ( !ok )
		{
			return false;
		}

		for ( GlobalFixture *gf = GlobalFixture::lastGlobalFixture();
			gf != 0; gf = gf->prevGlobalFixture() )
		{
			_TS_TRY_WITH_SIGNAL_PROTECTION
			{
				_TS_TRY
				{
					ok = gf->tearDown();
				}
				_TS_PROPAGATE_SIGNAL
				_TS_LAST_CATCH( {
					tracker().failedTest( file(), line(),
						"Exception thrown from GlobalFixture::tearDown()" );
					ok = false;
				} );
			}
			_TS_CATCH_SIGNAL({
				tracker().failedTest( file(), line(),
					__cxxtest_sigmsg.c_str() );
				ok = false;
			});

			if ( !ok )
			{
				return false;
			}
		}

		return ok;
	}

    bool RealWorldDescription::setUp()
    {
		for ( GlobalFixture *gf = GlobalFixture::firstGlobalFixture();
			gf != 0; gf = gf->nextGlobalFixture() )
		{
			bool ok = true;

			_TS_TRY_WITH_SIGNAL_PROTECTION
			{
				_TS_TRY
				{
					ok = gf->setUpWorld();
				}
				_TS_PROPAGATE_SIGNAL
				_TS_LAST_CATCH( {
					doWarn( __FILE__, 1, "Error setting up world" );
					ok = false;
				} );
			}
			_TS_CATCH_SIGNAL({
				doWarn( __FILE__, 1, __cxxtest_sigmsg.c_str() );
				ok = false;
			});

			if ( !ok )
			{
				return false;
			}
		}

		return true;
	}

    bool RealWorldDescription::tearDown()
    {
		for ( GlobalFixture *gf = GlobalFixture::lastGlobalFixture();
			gf != 0; gf = gf->prevGlobalFixture() )
		{
			bool ok = true;

			_TS_TRY_WITH_SIGNAL_PROTECTION
			{
				_TS_TRY
				{
					ok = gf->tearDownWorld();
				}
				_TS_PROPAGATE_SIGNAL
				_TS_LAST_CATCH( {
					doWarn( __FILE__, 1, "Error tearing down world" );
					ok = false;
				} );
			}
			_TS_CATCH_SIGNAL({
				doWarn( __FILE__, 1, __cxxtest_sigmsg.c_str() );
				ok = false;
			});

			if ( !ok )
			{
				return false;
			}
		}

		return true;
	}

    //
    // These are just nicer here
    //
    Link *List::head()
    {
        Link *l = _head;
        while ( l && !l->active() )
            l = l->next();
        return l;
    }

    const Link *List::head() const
    {
        Link *l = _head;
        while ( l && !l->active() )
            l = l->next();
        return l;
    }

    Link *List::tail()
    {
        Link *l = _tail;
        while ( l && !l->active() )
            l = l->prev();
        return l;
    }

    const Link *List::tail() const
    {
        Link *l = _tail;
        while ( l && !l->active() )
            l = l->prev();
        return l;
    }

    unsigned List::size() const
    {
        unsigned count = 0;
        for ( const Link *l = head(); l != 0; l = l->next() )
            ++ count;
        return count;
    }

    Link *List::nth( unsigned n )
    {
        Link *l = head();
        while ( n -- )
            l = l->next();
        return l;
    }

    void List::activateAll()
    {
        for ( Link *l = _head; l != 0; l = l->justNext() )
            l->setActive( true );
    }

    void List::leaveOnly( const Link &link )
    {
        for ( Link *l = head(); l != 0; l = l->next() )
            if ( l != &link )
                l->setActive( false );
    }

    //
    // Convert total tests to string
    //
#ifndef _CXXTEST_FACTOR
    char *WorldDescription::strTotalTests( char *s ) const
    {
        numberToString( numTotalTests(), s );
        return s;
    }
#else // _CXXTEST_FACTOR
    char *WorldDescription::strTotalTests( char *s ) const
    {
        char *p = numberToString( numTotalTests(), s );

        if ( numTotalTests() <= 1 )
            return s;

        unsigned n = numTotalTests();
        unsigned numFactors = 0;

        for ( unsigned factor = 2; (factor * factor) <= n; factor += (factor == 2) ? 1 : 2 ) {
            unsigned power;

            for ( power = 0; (n % factor) == 0; n /= factor )
                ++ power;

            if ( !power )
                continue;

            p = numberToString( factor, copyString( p, (numFactors == 0) ? " = " : " * " ) );
            if ( power > 1 )
                p = numberToString( power, copyString( p, "^" ) );
            ++ numFactors;
        }

        if ( n > 1 ) {
            if ( !numFactors )
                copyString( p, tracker().failedTests() ? " :(" : tracker().warnings() ? " :|" : " :)" );
            else
                numberToString( n, copyString( p, " * " ) );
        }
        return s;
    }
#endif // _CXXTEST_FACTOR

#   if defined(_CXXTEST_HAVE_EH)
    //
    // Test-aborting stuff
    //
    static bool currentAbortTestOnFail = false;

    bool abortTestOnFail()
    {
        return currentAbortTestOnFail;
    }

    void setAbortTestOnFail( bool value )
    {
        currentAbortTestOnFail = value;
    }

    void doAbortTest()
    {
        if ( currentAbortTestOnFail )
            throw AbortTest();
    }
#   endif // _CXXTEST_HAVE_EH

    //
    // Max dump size
    //
    static unsigned currentMaxDumpSize = CXXTEST_MAX_DUMP_SIZE;

    unsigned maxDumpSize()
    {
        return currentMaxDumpSize;
    }

    void setMaxDumpSize( unsigned value )
    {
        currentMaxDumpSize = value;
    }
};

#ifdef CXXTEST_TRAP_SIGNALS

#include <signal.h>
#include <sstream>

#ifndef HINT_PREFIX
#define HINT_PREFIX ""
#endif

extern "C" {

#ifndef _MSC_VER

#ifdef __CYGWIN__
    void __assert(const char* file, int line, const char* failedexpr)
#else
	void __eprintf(const char* fmt, const char* file, unsigned line, const char* failedexpr)
#endif // __CYGWIN__
    {
		CxxTest::StringAppender appender;
#ifdef CXXTEST_XML_OUTPUT
		CxxTest::__append_escaped_xml(HINT_PREFIX, appender);
		CxxTest::__append_escaped_xml("assertion \"");
		CxxTest::__append_escaped_xml(failedexpr);
		CxxTest::__append_escaped_xml("\" failed: file \"");
		CxxTest::__append_escaped_xml(file);
		CxxTest::__append_escaped_xml("\", line ");
		appender.append_num(line);
#else
		appender.append_str(HINT_PREFIX);
		appender.append_str("assertion \"");
		appender.append_str(failedexpr);
		appender.append_str("\" failed: file \"");
		appender.append_str(file);
		appender.append_str("\", line ");
		appender.append_num(line);
#endif

        CxxTest::__cxxtest_assertmsg = appender.str();
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

#endif // _MSC_VER

}

#endif // CXXTEST_TRAP_SIGNALS

#endif // __CXXTEST__ROOT_CPP
