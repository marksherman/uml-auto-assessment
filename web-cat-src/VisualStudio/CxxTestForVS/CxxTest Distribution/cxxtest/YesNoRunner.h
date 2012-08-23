#ifndef __CXXTEST__YESNORUNNER_H
#define __CXXTEST__YESNORUNNER_H

//
// The YesNoRunner is a simple TestListener that
// just returns true iff all tests passed.
//

#include <cxxtest/TestRunner.h>
#include <cxxtest/TestListener.h>

namespace CxxTest 
{
    class YesNoRunner : public TestListener
    {
    public:
        YesNoRunner()
        {
        }
        
        int run()
        {
            TestRunner::runAllTests( *this );
            return tracker().failedTests();
        }
    };
}

#endif // __CXXTEST__YESNORUNNER_H
