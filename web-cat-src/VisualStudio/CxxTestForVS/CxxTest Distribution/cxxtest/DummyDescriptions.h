#ifndef __CXXTEST__DUMMY_DESCRIPTIONS_H
#define __CXXTEST__DUMMY_DESCRIPTIONS_H

//
// DummyTestDescription, DummySuiteDescription and DummyWorldDescription
//

#include <cxxtest/Descriptions.h>

namespace CxxTest 
{
    class DummyTestDescription : public TestDescription
    {
    public:
        DummyTestDescription() {}
        
        const char *file() const { return "<no file>"; }
        unsigned line() const { return 0; }
        const char *testName() const { return "<no test>"; }
        const char *suiteName() const { return "<no suite>"; }
        bool setUp() { return true;}
        void run() {}
        bool tearDown() { return true;}

        TestDescription *next() { return 0; }
        const TestDescription *next() const { return 0; }
    };

    class DummySuiteDescription : public SuiteDescription
    {        
    public:
        DummySuiteDescription() : _test() {}
        
        const char *file() const { return "<no file>"; }
        unsigned line() const { return 0; }
        const char *suiteName() const { return "<no suite>"; }
        TestSuite *suite() const { return 0; }
        unsigned numTests() const { return 0; }
        const TestDescription &testDescription( unsigned ) const { return _test; }
        SuiteDescription *next() { return 0; }
        TestDescription *firstTest() { return 0; }
        const SuiteDescription *next() const { return 0; }
        const TestDescription *firstTest() const { return 0; }
        void activateAllTests() {}
        bool leaveOnly( const char * /*testName*/ ) { return false; }
        
        bool setUp() { return true;}
        bool tearDown() { return true;}

    private:
        DummyTestDescription _test;
    };

    class DummyWorldDescription : public WorldDescription
    {        
    public:
        DummyWorldDescription() : _suite() {}
        
        unsigned numSuites( void ) const { return 0; }
        unsigned numTotalTests( void ) const { return 0; }
        const SuiteDescription &suiteDescription( unsigned ) const { return _suite; }
        SuiteDescription *firstSuite() { return 0; }
        const SuiteDescription *firstSuite() const { return 0; }
        void activateAllTests() {}
        bool leaveOnly( const char * /*suiteName*/, const char * /*testName*/ = 0 ) { return false; }
            
        bool setUp() { return true;}
        bool tearDown() { return true;}

    private:
        DummySuiteDescription _suite;
    };
}

#endif // __CXXTEST__DUMMY_DESCRIPTIONS_H

