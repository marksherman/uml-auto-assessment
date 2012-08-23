#ifndef __cxxtest__WebCATPlistListener_h__
#define __cxxtest__WebCATPlistListener_h__

//
// This file defines a listener that can will dump its results out to a
// plist file that can be slurped into the Web-CAT grading.properties file
// for reporting purposes.
//

#include <cxxtest/TestListener.h>
#include <cxxtest/SafeString.h>
#include <dereferee.h>

#ifndef _MSC_VER
#   include <cxxtest/_WebCATPlistPOSIXErrorTranslator.h>
#else // !MSC_VER
//
// TODO: implement when we write a MSVC++ grading plugin
//
//#   include <cxxtest/_WebCATPlistWin32ErrorTranslator.h>
#endif // _MSC_VER

#include <iostream>
#include <fstream>
using namespace std;

namespace CxxTest
{
    enum {
        PLIST_CODE_PASS = 1,
        PLIST_CODE_FAILED_ASSERT,
        PLIST_CODE_FAILED_ASSERT_EQUALS,
        PLIST_CODE_FAILED_ASSERT_SAME_DATA,
        PLIST_CODE_FAILED_ASSERT_DELTA,
        PLIST_CODE_FAILED_ASSERT_DIFFERS,
        PLIST_CODE_FAILED_ASSERT_LESS_THAN,
        PLIST_CODE_FAILED_ASSERT_LESS_THAN_EQUALS,
        PLIST_CODE_FAILED_ASSERT_RELATION,
        PLIST_CODE_FAILED_ASSERT_PREDICATE,
        PLIST_CODE_FAILED_ASSERT_THROWS,
        PLIST_CODE_FAILED_ASSERT_THROWS_NOT,
        PLIST_CODE_GENERIC_FAILURE,
        PLIST_CODE_DIVISION_BY_ZERO,
        PLIST_CODE_BAD_ACCESS,
        PLIST_CODE_TIMEOUT,
        PLIST_CODE_OTHER_SIGNAL,
        PLIST_CODE_UNINITIALIZED_POINTER,
        PLIST_CODE_DELETED_POINTER,
        PLIST_CODE_OUT_OF_BOUNDS_POINTER,
        PLIST_CODE_NULL_POINTER,
        PLIST_CODE_OTHER_COMP_ARITH_ERROR,
        PLIST_CODE_WRONG_DELETE,
        PLIST_CODE_MISC_DEREFEREE_ERROR
    };


    class WebCATPlistListener :
        public TestListener,
        public WebCATPlistPlatformSpecificErrorTranslator
    {
    public:
        // ----------------------------------------------------------
        WebCATPlistListener()
        {
            firstTest = true;
            numTests = 0;
        }


        // ----------------------------------------------------------
        enum {
            GET = 0,
            SET
        };


        // ----------------------------------------------------------
        static int lastDerefereeError(int action, int code)
        {
            static int lastError = 0;
            
            if (action == SET)
                lastError = code;

            return lastError;
        }


        // ----------------------------------------------------------
        static int codeForDerefereeError(int code)
        {
            static const int errorCodes[] = {
                PLIST_CODE_MISC_DEREFEREE_ERROR,
                PLIST_CODE_UNINITIALIZED_POINTER,
                PLIST_CODE_DELETED_POINTER,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_WRONG_DELETE,
                PLIST_CODE_WRONG_DELETE,
                PLIST_CODE_UNINITIALIZED_POINTER,
                PLIST_CODE_UNINITIALIZED_POINTER,
                PLIST_CODE_DELETED_POINTER,
                PLIST_CODE_DELETED_POINTER,
                PLIST_CODE_UNINITIALIZED_POINTER,
                PLIST_CODE_UNINITIALIZED_POINTER,
                PLIST_CODE_UNINITIALIZED_POINTER,
                PLIST_CODE_DELETED_POINTER,
                PLIST_CODE_DELETED_POINTER,
                PLIST_CODE_DELETED_POINTER,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_NULL_POINTER,
                PLIST_CODE_NULL_POINTER,
                PLIST_CODE_NULL_POINTER,
                PLIST_CODE_UNINITIALIZED_POINTER,
                PLIST_CODE_DELETED_POINTER,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_UNINITIALIZED_POINTER,
                PLIST_CODE_DELETED_POINTER,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_NULL_POINTER,
                PLIST_CODE_OTHER_COMP_ARITH_ERROR,
                PLIST_CODE_UNINITIALIZED_POINTER,
                PLIST_CODE_DELETED_POINTER,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_NULL_POINTER,
                PLIST_CODE_NULL_POINTER,
                PLIST_CODE_OTHER_COMP_ARITH_ERROR,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_OUT_OF_BOUNDS_POINTER,
                PLIST_CODE_MISC_DEREFEREE_ERROR,
            };

            if (code >= 0 &&
                code < (int) (sizeof(errorCodes) / sizeof(int)))
            {
                return errorCodes[code];
            }
            else
            {
                return PLIST_CODE_MISC_DEREFEREE_ERROR;
            }
        }


        // ------------------------------------------------------
        static int levelOf(int code)
        {
            if (code <= 1)       return 1;
            else if (code <= 12) return 2;
            else if (code <= 17) return 3;
            else if (code <= 24) return 4;
            else                 return 0;
        }


        // ------------------------------------------------------
        void leaveWorld( const WorldDescription & /* desc */ )
        {
            const char* fragmentPath = NULL;

#ifdef WEBCAT_PLIST_FRAGMENT_PATH
            fragmentPath = WEBCAT_PLIST_FRAGMENT_PATH;
#endif
            fragmentPath = getenv("WEBCAT_PLIST_FRAGMENT_PATH");

            if (fragmentPath)
            {
                FILE* out = fopen(fragmentPath, "w");
                fprintf(out, "$results->addTestsExecuted(%d);\n", numTests);
                fprintf(out, "$results->addTestsFailed(%d);\n",
                    tracker().failedTests());
                fputs("$results->addToPlist( <<PLIST );\n", out);

                SafeString errorStr = perlEscape(errorPlist.c_str());
                fputs(errorStr.c_str(), out);
                
                fputs("\nPLIST\n", out);
            }
        }


        // ------------------------------------------------------
        static SafeString perlEscape(const char* str)
        {
            SafeString result;
            
            while (*str)
            {
                char ch = *str;
                switch (ch)
                {
                    case '@':
                    case '$':
                    case '%':
                    case '#':
                    case '"':
                    case '\\':
                        result += "\\";
                }

                result += ch;
                str++;
            }
            
            return result;
        }


        // ------------------------------------------------------
        static SafeString stringFromInt(int n)
        {
            const int BUFSIZE = 32;
            char buffer[BUFSIZE];
            snprintf(buffer, BUFSIZE, "%d", n);
            return SafeString(buffer);
        }


        // ------------------------------------------------------
        void enterSuite( const SuiteDescription& /* d */ )
        {
            lastDerefereeError(SET, -1);
        }


        // ------------------------------------------------------
        void enterTest( const TestDescription& /* d */ )
        {
            numTests++;
            lastDerefereeError(SET, -1);
            
            if (firstTest)
            {
                firstTest = false;
            }
            else
            {
                errorPlist += ",";
            }
        }


        // ------------------------------------------------------
        void leaveTest( const TestDescription& /* d */ )
        {
            if (!tracker().testFailed())
            {
                errorPlist += "{suite=\"";
                errorPlist += tracker().test().suiteName();
                errorPlist += "\"; test=\"";
                errorPlist += tracker().test().testName();
                errorPlist += "\"; level=1";
                errorPlist += "; code=1";
                errorPlist += ";}";
            }
        }


        // ------------------------------------------------------
        void suiteInitError( const char * /* file */, unsigned /* line */,
                             const char * /* expression */ )
        {
            int code, detailCode;
            getFailureCodes(code, detailCode);

            int level = levelOf(code);

            errorPlist += "{suite=\"";
            errorPlist += tracker().suite().suiteName();
            errorPlist += "\"; test=\"<init>\"; level=";
            errorPlist += stringFromInt(level);
            errorPlist += "; code=";
            errorPlist += stringFromInt(code);
                
            if (detailCode != -1)
            {
                errorPlist += "; detailCode=";
                errorPlist += stringFromInt(detailCode);
            }

            errorPlist += ";}";
        }


        // ------------------------------------------------------
        void failedTest( const char * /* file */, unsigned /* line */,
                         const char * /* expression */ )
        {
            int code, detailCode;
            getFailureCodes(code, detailCode);

            int level = levelOf(code);

            errorPlist += "{suite=\"";
            errorPlist += tracker().test().suiteName();
            errorPlist += "\"; test=\"";
            errorPlist += tracker().test().testName();
            errorPlist += "\"; level=";
            errorPlist += stringFromInt(level);
            errorPlist += "; code=";
            errorPlist += stringFromInt(code);
                
            if (detailCode != -1)
            {
                errorPlist += "; detailCode=";
                errorPlist += stringFromInt(detailCode);
            }

            errorPlist += ";}";
        }


        // ------------------------------------------------------
        void getFailureCodes(int& code, int& detailCode)
        {
            code = PLIST_CODE_GENERIC_FAILURE;
            detailCode = -1;

            int lastDerefError = lastDerefereeError(GET, 0);
            if (lastDerefError != -1)
            {
                code = codeForDerefereeError(lastDerefError);
                detailCode = lastDerefError;
            }
            else
            {
                tryToGetPlatformSpecificErrorInfo(code, detailCode);
            }
        }


        // ------------------------------------------------------
        void failedAssert( const char * /* file */, unsigned /* line */,
                           const char * /* expression */)
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT);
        }


        // ------------------------------------------------------
        void failedAssertEquals( const char * /* file */, unsigned /* line */,
                                 const char * /* xStr */, const char * /* yStr */,
                                 const char * /* x */, const char * /* y */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_EQUALS);
        }


        // ------------------------------------------------------
        void failedAssertSameData( const char * /* file */, unsigned /* line */,
                                   const char * /* xStr */, const char * /* yStr */,
                                   const char * /* sizeStr */, const void * /* x */,
                                   const void * /* y */, unsigned /* size */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_SAME_DATA);
        }


        // ------------------------------------------------------
        void failedAssertDelta( const char * /* file */, unsigned  /* line */,
                                const char * /* xStr */, const char * /* yStr */,
                                const char * /* dStr */, const char * /* x */,
                                const char * /* y */, const char * /* d */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_DELTA);
        }


        // ------------------------------------------------------
        void failedAssertDiffers( const char * /* file */, unsigned /* line */,
                                  const char * /* xStr */, const char * /* yStr */,
                                  const char * /* value */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_DIFFERS);
        }


        // ------------------------------------------------------
        void failedAssertLessThan( const char * /* file */, unsigned /* line */,
                                   const char * /* xStr */, const char * /* yStr */,
                                   const char * /* x */, const char * /* y */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_LESS_THAN);
        }


        // ------------------------------------------------------
        void failedAssertLessThanEquals( const char * /* file */, unsigned /* line */,
                                         const char * /* xStr */, const char * /* yStr */,
                                         const char * /* x */, const char * /* y */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_LESS_THAN_EQUALS);
        }


        // ------------------------------------------------------
        void failedAssertRelation( const char * /* file */, unsigned /* line */,
                                   const char * /* relation */, const char * /* xStr */,
                                   const char * /* yStr */, const char * /* x */,
                                   const char * /* y */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_RELATION);
        }


        // ------------------------------------------------------
        void failedAssertPredicate( const char * /* file */, unsigned /* line */,
                                    const char * /* predicate */, const char * /* xStr */,
                                    const char * /* x */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_PREDICATE);
        }


        // ------------------------------------------------------
        void failedAssertThrows( const char * /* file */, unsigned /* line */,
                                 const char * /* expression */, const char * /* type */,
                                 bool /* otherThrown */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_THROWS);
        }


        // ------------------------------------------------------
        void failedAssertThrowsNot( const char * /* file */, unsigned /* line */,
                                    const char * /* expression */ )
        {
            writeTestFailedError(PLIST_CODE_FAILED_ASSERT_THROWS_NOT);
        }


    private:
        // ------------------------------------------------------
        void writeTestFailedError(int code)
        {
            int level = levelOf(code);
            
            errorPlist += "{suite=\"";
            errorPlist += tracker().test().suiteName();
            errorPlist += "\"; test=\"";
            errorPlist += tracker().test().testName();
            errorPlist += "\"; level=";
            errorPlist += stringFromInt(level);
            errorPlist += "; code=";
            errorPlist += stringFromInt(code);
            errorPlist += ";}";
        }

        int numTests;
        bool firstTest;
        SafeString errorPlist;
    };
    
} // end namespace CxxTest

#undef QUOTE

#endif // __cxxtest__WebCATPlistListener_h__
