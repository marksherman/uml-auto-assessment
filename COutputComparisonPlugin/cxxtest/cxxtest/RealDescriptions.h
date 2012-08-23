#ifndef __CXXTEST__REAL_DESCRIPTIONS_H
#define __CXXTEST__REAL_DESCRIPTIONS_H

//
// The "real" description classes
//


#include <cxxtest/Descriptions.h>
#include <cxxtest/TestSuite.h>
#include <cxxtest/GlobalFixture.h>
#include <cxxtest/Signals.h>

namespace CxxTest 
{
    class RealTestDescription : public TestDescription
    {
    public:
        RealTestDescription()
        {
        }

        RealTestDescription( List &argList, SuiteDescription &argSuite, unsigned argLine, const char *argTestName )
        {
            initialize( argList, argSuite, argLine, argTestName );
        }

        void initialize( List &argList, SuiteDescription &argSuite, unsigned argLine, const char *argTestName )
        {
            _suite = &argSuite;
            _line = argLine;
            _testName = argTestName;
            attach( argList );
        }

        const char *file() const { return _suite->file(); }
        unsigned line() const { return _line; }
        const char *testName() const { return _testName; }
        const char *suiteName() const { return _suite->suiteName(); }

        TestDescription *next() { return (RealTestDescription *)Link::next(); }
        const TestDescription *next() const { return (const RealTestDescription *)Link::next(); }

        TestSuite *suite() const { return _suite->suite(); }

        void run()
        {
	    _TS_TRY_WITH_SIGNAL_PROTECTION
	    {
                _TS_TRY
	        {
		  runTest();
	        }
                _TS_CATCH_ABORT( {} )
                ___TSM_CATCH( file(), line(), "Exception thrown from test" );
	    }
	    _TS_CATCH_SIGNAL({
	        tracker().failedTest(
	      	    file(), line(), __cxxtest_sigmsg.c_str() );
	    });
        }

        bool setUp();
        bool tearDown();

    private:
        RealTestDescription( const RealTestDescription & );
        RealTestDescription &operator=( const RealTestDescription & );

        virtual void runTest() = 0;

        SuiteDescription *_suite;
        unsigned _line;
        const char *_testName;
    };

    class RealSuiteDescription : public SuiteDescription
    {
    public:
        RealSuiteDescription() {}
        RealSuiteDescription( const char *argFile, unsigned argLine, const char *argSuiteName, List &argTests )
        {
            initialize( argFile, argLine, argSuiteName, argTests );
        }

        void initialize( const char *argFile, unsigned argLine, const char *argSuiteName, List &argTests )
        {
            _file = argFile;
            _line = argLine;
            _suiteName = argSuiteName;
            _tests = &argTests;

            attach( _suites );
        }

        const char *file() const { return _file; }
        unsigned line() const { return _line; }
        const char *suiteName() const { return _suiteName; }

        TestDescription *firstTest() { return (RealTestDescription *)_tests->head(); }
        const TestDescription *firstTest() const { return (const RealTestDescription *)_tests->head(); }
        SuiteDescription *next() { return (RealSuiteDescription *)Link::next(); }
        const SuiteDescription *next() const { return (const RealSuiteDescription *)Link::next(); }

        unsigned numTests() const { return _tests->size(); }
        const TestDescription &testDescription( unsigned i ) const { return *(RealTestDescription *)_tests->nth( i ); }

        void activateAllTests()
        {
            _tests->activateAll();
        }

        bool leaveOnly( const char *testName )
        {
            for ( TestDescription *td = firstTest(); td != 0; td = td->next() ) {
                if ( stringsEqual( td->testName(), testName ) ) {
                    _tests->leaveOnly( *td );
                    return true;
                }
            }
            return false;        
        }

    private:
        RealSuiteDescription( const RealSuiteDescription & );
        RealSuiteDescription &operator=( const RealSuiteDescription & );

        const char *_file;
        unsigned _line;
        const char *_suiteName;
        List *_tests;

        static List _suites;
        friend class RealWorldDescription;
    };

    class StaticSuiteDescription : public RealSuiteDescription
    {
    public:
        StaticSuiteDescription() {}
        StaticSuiteDescription( const char *argFile, unsigned argLine,
                                const char *argSuiteName, TestSuite &argSuite,
                                List &argTests ) :
            RealSuiteDescription( argFile, argLine, argSuiteName, argTests )
        {
            initializeStatic( argSuite );
        }

        void initialize( const char *argFile, unsigned argLine,
                         const char *argSuiteName, TestSuite &argSuite,
                         List &argTests )
        {
            RealSuiteDescription::initialize( argFile, argLine, argSuiteName, argTests );
            initializeStatic( argSuite );
        }

        void initializeStatic( TestSuite &argSuite )
        {
            _suite = &argSuite;
        }

        TestSuite *suite() const
        {
            return _suite;
        }

        bool setUp() { return true; }
        bool tearDown() { return true; }

    private:
        StaticSuiteDescription( const StaticSuiteDescription & );
        StaticSuiteDescription &operator=( const StaticSuiteDescription & );

        TestSuite *_suite;
    };

    template<class S>
    class DynamicSuiteDescription : public RealSuiteDescription
    {
    public:
        DynamicSuiteDescription() {}
        DynamicSuiteDescription( const char *argFile, unsigned argLine,
                                 const char *argSuiteName, List &argTests,
                                 S *&argSuite, unsigned argCreateLine,
                                 unsigned argDestroyLine ) :
            RealSuiteDescription( argFile, argLine, argSuiteName, argTests )
        {
            initializeDynamic( argSuite, argCreateLine, argDestroyLine );
        }

        void initialize( const char *argFile, unsigned argLine,
                         const char *argSuiteName, List &argTests,
                         S *&argSuite, unsigned argCreateLine,
                         unsigned argDestroyLine )
        {
            RealSuiteDescription::initialize( argFile, argLine, argSuiteName, argTests );
            initializeDynamic( argSuite, argCreateLine, argDestroyLine );
        }

        void initializeDynamic( S *&argSuite, unsigned argCreateLine, unsigned argDestroyLine )
        {
            _suite = &argSuite;
            _createLine = argCreateLine;
            _destroyLine = argDestroyLine;
        }

        TestSuite *suite() const
        {
            return realSuite();
        }

        bool setUp();
        bool tearDown();

    private:
        S *realSuite() const { return *_suite; }
        void setSuite( S *s ) { *_suite = s; }

        void createSuite()
        {
            setSuite( S::createSuite() );
        }

        void destroySuite()
        {
            S *s = realSuite();
            setSuite( 0 );
            S::destroySuite( s );
        }

        S **_suite;
        unsigned _createLine, _destroyLine;
    };

    template<class S>
    bool DynamicSuiteDescription<S>::setUp()
    {
	_TS_TRY_WITH_SIGNAL_PROTECTION
	{
            _TS_TRY {
                _TSM_ASSERT_THROWS_NOTHING( file(), _createLine,
		    "Exception thrown from createSuite()", createSuite() );
            }
            _TS_CATCH_ABORT( { setSuite( 0 ); } );
	}
	_TS_CATCH_SIGNAL({
	    setSuite( 0 );
            tracker().failedTest( file(), _createLine, __cxxtest_sigmsg );
	});

        return (suite() != 0);
    }

    template<class S>
    bool DynamicSuiteDescription<S>::tearDown()
    {
        if ( !_suite )
            return true;

	bool result = true;
	_TS_TRY_WITH_SIGNAL_PROTECTION
	{
            _TS_TRY {
                _TSM_ASSERT_THROWS_NOTHING( file(), _destroyLine,
		    "destroySuite() failed", destroySuite() );
	    }
            _TS_CATCH_ABORT( { result = false; } );
        }
	_TS_CATCH_SIGNAL({
	    result = false;
            tracker().failedTest( file(), _destroyLine, __cxxtest_sigmsg );
	});

        return result;
    }

    class RealWorldDescription : public WorldDescription
    {
    public:
        static List &suites()
        {
            return RealSuiteDescription::_suites;
        }

        unsigned numSuites( void ) const
        {
            return suites().size();
        }

        unsigned numTotalTests( void ) const
        {
            unsigned count = 0;
            for ( const SuiteDescription *sd = firstSuite();
		  sd != 0; sd = sd->next() )
                count += sd->numTests();
            return count;
        }

        SuiteDescription *firstSuite()
        {
            return (RealSuiteDescription *)suites().head();
        }

        const SuiteDescription *firstSuite() const
        {
            return (const RealSuiteDescription *)suites().head();
        }

        const SuiteDescription &suiteDescription( unsigned i ) const
        {
            return *(const RealSuiteDescription *)suites().nth( i );
        }

        void activateAllTests()
        {
            suites().activateAll();
            for ( SuiteDescription *sd = firstSuite();
		  sd != 0; sd = sd->next() )
                sd->activateAllTests();
        }

        bool leaveOnly( const char *suiteName, const char *testName = 0 )
        {
            for ( SuiteDescription *sd = firstSuite(); sd != 0; sd = sd->next() ) {
                if ( stringsEqual( sd->suiteName(), suiteName ) ) {
                    if ( testName )
                        if ( !sd->leaveOnly( testName ) )
                            return false;
                    suites().leaveOnly( *sd );
                    return true;
                }
            }
            return false;
        }

        bool setUp();
        bool tearDown();
    };

    inline void activateAllTests()
    {
        RealWorldDescription().activateAllTests();
    }

    inline bool leaveOnly( const char *suiteName, const char *testName = 0 )
    {
        return RealWorldDescription().leaveOnly( suiteName, testName );
    }
}

#endif // __CXXTEST__REAL_DESCRIPTIONS_H
