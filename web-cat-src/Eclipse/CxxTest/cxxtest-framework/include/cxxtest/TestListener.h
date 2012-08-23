#ifndef __cxxtest__TestListener_h__
#define __cxxtest__TestListener_h__

//
// TestListener is the base class for all "listeners",
// i.e. classes that receive notifications of the
// testing process.
//
// The names of the parameters are in comments to avoid
// "unused parameter" warnings.
//

#include <cstddef>
#include <cstdlib>
#include <cxxtest/Descriptions.h>
#include <cxxtest/Signals.h>
#include <dereferee.h>

namespace CxxTest
{
    extern bool filter_backtrace_frame(const char* function);

    class TestListener
    {
    public:
        TestListener() {}
    	virtual ~TestListener() {}

        virtual void enterWorld( const WorldDescription & /*desc*/ ) {}
        virtual void enterSuite( const SuiteDescription & /*desc*/ ) {}
        virtual void enterTest( const TestDescription & /*desc*/ ) {}
        virtual void trace( const char * /*file*/, unsigned /*line*/,
                            const char * /*expression*/ ) {}
        virtual void warning( const char * /*file*/, unsigned /*line*/,
                              const char * /*expression*/ ) {}
        virtual void failedTest( const char * /*file*/, unsigned /*line*/,
                                 const char * /*expression*/ ) {}
        virtual void failedAssert( const char * /*file*/, unsigned /*line*/,
                                   const char * /*expression*/ ) {}
        virtual void failedAssertEquals( const char * /*file*/, unsigned /*line*/,
                                         const char * /*xStr*/, const char * /*yStr*/,
                                         const char * /*x*/, const char * /*y*/ ) {}
        virtual void failedAssertSameData( const char * /*file*/, unsigned /*line*/,
                                           const char * /*xStr*/, const char * /*yStr*/,
                                           const char * /*sizeStr*/, const void * /*x*/,
                                           const void * /*y*/, unsigned /*size*/ ) {}
        virtual void failedAssertDelta( const char * /*file*/, unsigned /*line*/,
                                        const char * /*xStr*/, const char * /*yStr*/,
                                        const char * /*dStr*/, const char * /*x*/,
                                        const char * /*y*/, const char * /*d*/ ) {}
        virtual void failedAssertDiffers( const char * /*file*/, unsigned /*line*/,
                                          const char * /*xStr*/, const char * /*yStr*/,
                                          const char * /*value*/ ) {}
        virtual void failedAssertLessThan( const char * /*file*/, unsigned /*line*/,
                                           const char * /*xStr*/, const char * /*yStr*/,
                                           const char * /*x*/, const char * /*y*/ ) {}
        virtual void failedAssertLessThanEquals( const char * /*file*/, unsigned /*line*/,
                                                 const char * /*xStr*/, const char * /*yStr*/,
                                                 const char * /*x*/, const char * /*y*/ ) {}
        virtual void failedAssertPredicate( const char * /*file*/, unsigned /*line*/,
                                            const char * /*predicate*/, const char * /*xStr*/, const char * /*x*/ ) {}
        virtual void failedAssertRelation( const char * /*file*/, unsigned /*line*/,
                                           const char * /*relation*/, const char * /*xStr*/, const char * /*yStr*/,
                                           const char * /*x*/, const char * /*y*/ ) {}
        virtual void failedAssertThrows( const char * /*file*/, unsigned /*line*/,
                                         const char * /*expression*/, const char * /*type*/,
                                         bool /*otherThrown*/ ) {}
        virtual void failedAssertThrowsNot( const char * /*file*/, unsigned /*line*/,
                                            const char * /*expression*/ ) {}
        virtual void leaveTest( const TestDescription & /*desc*/ ) {}
        virtual void leaveSuite( const SuiteDescription & /*desc*/ ) {}
        virtual void leaveWorld( const WorldDescription & /*desc*/ ) {}
        virtual void suiteInitError( const char * /*file*/, unsigned /*line*/, const char * /* expression */ ) {}


        // ------------------------------------------------------
        virtual void walkLastBacktrace()
        {
#ifdef CXXTEST_TRACE_STACK
            Dereferee::platform* platform = Dereferee::current_platform();
            void **bt = CxxTest::__cxxtest_sig_backtrace;
            bool btNeedsFree = false;

            if (!bt)
            {
                bt = platform->get_backtrace(NULL, NULL);
                btNeedsFree = true;
            }

            if (bt)
            {
                char function[DEREFEREE_MAX_FUNCTION_LEN];
                char filename[DEREFEREE_MAX_FILENAME_LEN];
                int line_number;
                int index = 0;

                while(*bt)
                {
                    if(platform->get_backtrace_frame_info(*bt,
                        function, filename, &line_number))
                    {
                        if(CxxTest::filter_backtrace_frame(function))
                        {
                            bool cont = visitBacktraceFrame(index, *bt,
                                function, filename, line_number);
                                
                            if (!cont)
                                break;
                        }
                    }

                    bt++;
                    index++;
                }
            }
            
            if (btNeedsFree)
            {
                platform->free_backtrace(bt);
            }
#endif
        }


        // ------------------------------------------------------
        virtual bool visitBacktraceFrame(int index, void* frame,
                                          const char* function,
                                          const char* filename,
                                          int lineNumber)
        {
            return true;
        }

        //
        // Class-specific overloads so that listeners do not get tracked by
        // the Dereferee memory manager, if Dereferee is also in use.
        //
        void* operator new(size_t size) { return malloc(size); }
        void operator delete(void* ptr) { free(ptr); }
    };
}

#endif // __cxxtest__TestListener_h__
