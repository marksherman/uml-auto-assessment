#ifndef __TESTTRACKER_H
#define __TESTTRACKER_H

//
// The TestTracker tracks running tests
// The actual work is done in CountingListenerProxy,
// but this way avoids cyclic references TestListener<->CountingListenerProxy
//

#include <cxxtest/TestListener.h>
#include <cxxtest/DummyDescriptions.h>

namespace CxxTest
{
    class TestListener;
    
    class TestTracker : public TestListener
    {
    public:
        virtual ~TestTracker() {}
        
        static TestTracker &tracker()
        {
            static TestTracker theTracker;
            return theTracker;
        }

        const TestDescription *fixTest( const TestDescription *d ) const { return d ? d : &dummyTest(); }
        const SuiteDescription *fixSuite( const SuiteDescription *d ) const { return d ? d : &dummySuite(); }
        const WorldDescription *fixWorld( const WorldDescription *d ) const { return d ? d : &dummyWorld(); }

        const TestDescription &test() const { return *_test; }
        const SuiteDescription &suite() const { return *_suite; }
        const WorldDescription &world() const { return *_world; }
        
        bool testFailed() const { return (testFailedAsserts() > 0); }
        bool suiteFailed() const { return (suiteFailedTests() > 0); }
        bool worldFailed() const { return (failedSuites() > 0); }
        
        unsigned warnings() const { return _warnings; }
        unsigned failedTests() const { return _failedTests; }
        unsigned testFailedAsserts() const { return _testFailedAsserts; }
        unsigned suiteFailedTests() const { return _suiteFailedTests; }
        unsigned failedSuites() const { return _failedSuites; }

        void enterWorld( const WorldDescription &wd )
        {
            setWorld( &wd );
            _warnings = _failedTests = _testFailedAsserts = _suiteFailedTests = _failedSuites = 0;
            _l->enterWorld( wd );
        }

        void enterSuite( const SuiteDescription &sd )
        {
            setSuite( &sd );
            _testFailedAsserts = _suiteFailedTests = 0;
            _l->enterSuite(sd);
        }
        
        void enterTest( const TestDescription &td )
        {
            setTest( &td );
            _testFailedAsserts = false;
            _l->enterTest(td);
        }

        void leaveTest( const TestDescription &td )
        {
            _l->leaveTest( td );
            setTest( 0 );
        }

        void leaveSuite( const SuiteDescription &sd )
        {
            _l->leaveSuite( sd );
            setSuite( 0 );
        }

        void leaveWorld( const WorldDescription &wd )
        {
            _l->leaveWorld( wd );
            setWorld( 0 );
        }

        void trace( const char *file, unsigned line, const char *expression )
        {
            _l->trace( file, line, expression );
        }

        void warning( const char *file, unsigned line, const char *expression )
        {
            countWarning();
            _l->warning( file, line, expression );
        }

        void failedTest( const char *file, unsigned line, const char *expression )
        {
            countFailure();
            _l->failedTest( file, line, expression );
        }
        
        void failedAssert( const char *file, unsigned line, const char *expression )
        {
            countFailure();
            _l->failedAssert( file, line, expression );
        }

        void failedAssertEquals( const char *file, unsigned line,
                                 const char *xStr, const char *yStr,
                                 const char *x, const char *y )
        {
            countFailure();
            _l->failedAssertEquals( file, line, xStr, yStr, x, y );
        }

        void failedAssertSameData( const char *file, unsigned line,
                                   const char *xStr, const char *yStr,
                                   const char *sizeStr, const void *x,
                                   const void *y, unsigned size )
        {
            countFailure();
            _l->failedAssertSameData( file, line, xStr, yStr, sizeStr, x, y, size );
        }

        void failedAssertDelta( const char *file, unsigned line,
                                const char *xStr, const char *yStr, const char *dStr,
                                const char *x, const char *y, const char *d )
        {
            countFailure();
            _l->failedAssertDelta( file, line, xStr, yStr, dStr, x, y, d );
        }
        void failedAssertDiffers( const char *file, unsigned line,
                                  const char *xStr, const char *yStr,
                                  const char *value )
        {
            countFailure();
            _l->failedAssertDiffers( file, line, xStr, yStr, value );
        }
        
        void failedAssertLessThan( const char *file, unsigned line,
                                   const char *xStr, const char *yStr,
                                   const char *x, const char *y )
        {
            countFailure();
            _l->failedAssertLessThan( file, line, xStr, yStr, x, y );
        }

        void failedAssertLessThanEquals( const char *file, unsigned line,
                                         const char *xStr, const char *yStr,
                                         const char *x, const char *y )
        {
            countFailure();
            _l->failedAssertLessThanEquals( file, line, xStr, yStr, x, y );
        }

        void failedAssertPredicate( const char *file, unsigned line,
                                    const char *predicate, const char *xStr, const char *x )
        {
            countFailure();
            _l->failedAssertPredicate( file, line, predicate, xStr, x );
        }
        
        void failedAssertRelation( const char *file, unsigned line,
                                   const char *relation, const char *xStr, const char *yStr,
                                   const char *x, const char *y )
        {
            countFailure();
            _l->failedAssertRelation( file, line, relation, xStr, yStr, x, y );
        }
        
        void failedAssertThrows( const char *file, unsigned line,
                                 const char *expression, const char *type,
                                 bool otherThrown )
        {
            countFailure();
            _l->failedAssertThrows( file, line, expression, type, otherThrown );
        }
        
        void failedAssertThrowsNot( const char *file, unsigned line, const char *expression )
        {
            countFailure();
            _l->failedAssertThrowsNot( file, line, expression );
        }

        void suiteInitError(const char *file, unsigned line, const char *expression)
        {
        	_l->suiteInitError(file, line, expression);
        }

    private:
        TestTracker( const TestTracker & );
        TestTracker &operator=( const TestTracker & );

        static bool _created;
        TestListener _dummyListener;
        DummyWorldDescription _dummyWorld;
        unsigned _warnings, _failedTests, _testFailedAsserts, _suiteFailedTests, _failedSuites;
        TestListener *_l;
        const WorldDescription *_world;
        const SuiteDescription *_suite;
        const TestDescription *_test;

        const TestDescription &dummyTest() const { return dummySuite().testDescription(0); }
        const SuiteDescription &dummySuite() const { return dummyWorld().suiteDescription(0); }
        const WorldDescription &dummyWorld() const { return _dummyWorld; }
        
        void setWorld( const WorldDescription *w )
        {
            _world = fixWorld( w );
            setSuite( 0 );
        }

        void setSuite( const SuiteDescription *s )
        {
            _suite = fixSuite( s );
            setTest( 0 );
        }

        void setTest( const TestDescription *t )
        {
            _test = fixTest( t );
        }

        void countWarning()
        {
            ++ _warnings;
        }

        void countFailure()
        {
            if ( ++ _testFailedAsserts == 1 ) {
                ++ _failedTests;
                if ( ++ _suiteFailedTests == 1 )
                    ++ _failedSuites;
            }
        }

        friend class TestRunner;
        
        TestTracker()
        {
            if ( !_created ) {
                initialize();
                _created = true;
            }
        }

        void initialize()
        {
            _warnings = 0;
            _failedTests = 0;
            _testFailedAsserts = 0;
            _suiteFailedTests = 0;
            _failedSuites = 0;
            setListener( 0 );
            _world = 0;
            _suite = 0;
            _test = 0;
        }

        void setListener( TestListener *l )
        {
            _l = l ? l : &_dummyListener;
        }
    };

    inline TestTracker &tracker() { return TestTracker::tracker(); }
};


#endif // __TESTTRACKER_H
