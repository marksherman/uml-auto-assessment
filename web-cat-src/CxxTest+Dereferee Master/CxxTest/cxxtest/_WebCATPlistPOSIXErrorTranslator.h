#ifndef __cxxtest___WebCATPlistPOSIXErrorTranslator_h__
#define __cxxtest___WebCATPlistPOSIXErrorTranslator_h__

//
// This file defines a listener that can will dump its results out to a
// plist file that can be slurped into the Web-CAT grading.properties file
// for reporting purposes.
//

#include <cxxtest/_SignalsPOSIX.h>

namespace CxxTest
{
    class WebCATPlistPOSIXErrorTranslator
    {
    public:
        static void tryToGetPlatformSpecificErrorInfo(int& code,
                                                      int& detailCode)
        {
            if (__cxxtest_last_signal)
            {
                switch (__cxxtest_last_signal)
                {
                    case SIGFPE:
                        code = 14; // PLIST_CODE_DIVISION_BY_ZERO
                        break;
                        
                    case SIGSEGV:
                    case SIGBUS:
                    case SIGILL:
                        code = 15; // PLIST_CODE_BAD_ACCESS
                        break;
                        
                    case SIGALRM:
                        code = 16; // PLIST_CODE_TIMEOUT
                        break;
                        
                    default:
                        code = 17; // PLIST_CODE_OTHER_SIGNAL
                        break;
                }

                detailCode = __cxxtest_last_signal;
            }
        }
    };
    
    typedef WebCATPlistPOSIXErrorTranslator
        WebCATPlistPlatformSpecificErrorTranslator;

} // end namespace CxxTest


#endif // __cxxtest___WebCATPlistPOSIXErrorTranslator_h__
