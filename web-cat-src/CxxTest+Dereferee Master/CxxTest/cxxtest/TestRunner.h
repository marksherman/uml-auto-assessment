#ifndef __cxxtest__TestRunner_h__
#define __cxxtest__TestRunner_h__

//
// TestRunner is the class that runs all the tests.
// To use it, create an object that implements the TestListener
// interface and call TestRunner::runAllTests( myListener );
// 

#include <cxxtest/TestListener.h>
#include <cxxtest/RealDescriptions.h>
#include <cxxtest/TestSuite.h>
#include <cxxtest/TestTracker.h>
#include <cxxtest/SuiteInitFailureTable.h>

namespace CxxTest 
{
    class TestRunner
    {
    public:
        static void runAllTests( TestListener &listener )
        {
            tracker().setListener( &listener );

            _TS_TRY
            {
                TestRunner().runWorld();
            }
            _TS_LAST_CATCH( {
                tracker().failedTest( __FILE__, __LINE__,
                    "Exception thrown from world" );
            } );

            tracker().setListener( 0 );
        }

        static void runAllTests( TestListener *listener )
        {
            if ( listener )
            {
                listener->warning( __FILE__, __LINE__,
                    "Deprecated; Use runAllTests( TestListener & )" );

                runAllTests( *listener );
            }
        }        
    
    private:
        void runWorld()
        {
            RealWorldDescription wd;
            WorldGuard sg;
            
            tracker().enterWorld( wd );
            if ( wd.setUp() ) {
                for ( SuiteDescription *sd = wd.firstSuite(); sd; sd = sd->next() )
                    if ( sd->active() )
                        runSuite( *sd );
            
                wd.tearDown();
            }
            tracker().leaveWorld( wd );
        }
    
        void runSuite( SuiteDescription &sd )
        {
            StateGuard sg;
            
            tracker().enterSuite( sd );

            // 2006-10-05 (aallowat): The signal handler keeps track of all
            // test suites that fail at static initialization time (i.e., a
            // field constructor raises a signal). If this suite is one of
            // those that failed, bail out, but dump a message to the
            // listener that notifies the user.
            const char* reason =
                CxxTest::__cxxtest_failed_init_suites.didSuiteFail(
                    sd.suiteName());

            if(reason)
            {
                tracker().suiteInitError(sd.file(), sd.line(), reason);
                tracker().leaveSuite( sd );
                return;
            }

            if ( sd.setUp() ) {
                for ( TestDescription *td = sd.firstTest(); td; td = td->next() )
                    if ( td->active() )
                        runTest( *td );

                sd.tearDown();
            }

            tracker().leaveSuite( sd );
        }

        void runTest( TestDescription &td )
        {
            StateGuard sg;
            
            tracker().enterTest( td );

            if ( td.setUp() ) {
                td.run();
                td.tearDown();
            }

            tracker().leaveTest( td );
        }
        
        class StateGuard
        {
#ifdef _CXXTEST_HAVE_EH
            bool _abortTestOnFail;
#endif // _CXXTEST_HAVE_EH
            unsigned _maxDumpSize;
            
        public:
            StateGuard()
            {
#ifdef _CXXTEST_HAVE_EH
                _abortTestOnFail = abortTestOnFail();
#endif // _CXXTEST_HAVE_EH
                _maxDumpSize = maxDumpSize();
            }
            
            ~StateGuard()
            {
#ifdef _CXXTEST_HAVE_EH
                setAbortTestOnFail( _abortTestOnFail );
#endif // _CXXTEST_HAVE_EH
                setMaxDumpSize( _maxDumpSize );
            }
        };

        class WorldGuard : public StateGuard
        {
        public:
            WorldGuard() : StateGuard()
            {
#ifdef _CXXTEST_HAVE_EH
                setAbortTestOnFail( CXXTEST_DEFAULT_ABORT );
#endif // _CXXTEST_HAVE_EH
                setMaxDumpSize( CXXTEST_MAX_DUMP_SIZE );
            }
        };
    };

    //
    // For --no-static-init
    //
    void initialize();
};


#endif // __cxxtest__TestRunner_h__
