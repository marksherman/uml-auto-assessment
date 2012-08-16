#ifndef __cxxtest___SignalsWin32_h__
#define __cxxtest___SignalsWin32_h__

#ifndef _WIN32_WINNT
#   define _WIN32_WINNT 0x400
#endif

#include <windows.h>
#include <excpt.h>

namespace CxxTest
{
    //
    // On Windows, we wrap SEH exceptions as C++ exceptions (compile with
    // /EHa) and handle them that way instead of using signals, which aren't
    // implemented as well on Windows.
    //

    class __se_exception
    {
    public:
        __se_exception(unsigned int code, EXCEPTION_POINTERS* info)
        {
            _code = code;
            _info = info;
        }

        unsigned int code() const { return _code; }
        EXCEPTION_POINTERS* info() const { return _info; }

    private:
        unsigned int _code;
        EXCEPTION_POINTERS* _info;
    };


    extern void __cxxtest_se_handler(__se_exception& e);


    //
    // _TS_PROPAGATE_SIGNAL is required to ensure that, for Windows, a
    // _TRY block inside a _TRY_WITH_SIGNAL_PROTECTION block does not
    // swallow the structured exception that represents the "signal" that
    // we're trying to catch. Code using these blocks together should be
    // structured as follows:
    //
    // _TS_TRY_WITH_SIGNAL_PROTECTION {
    //     _TRY {
    //         statements...
    //     }
    //     _TS_PROPAGATE_SIGNAL
    //     _TS_CATCH...
    // }
    // _TS_CATCH_SIGNAL...
    // 
    // On non-Win32 systems, the PROPAGATE directive is a no-op, since
    // signal protection there uses setjmp/longjmp and this propagation
    // is unnecessary.
    //

    // ----------------------------------------------------------
#ifdef CXXTEST_TRACE_STACK

    #define _TS_FREE_BACKTRACE \
        Dereferee::current_platform()->free_backtrace( \
            CxxTest::__cxxtest_sig_backtrace)

#else // !CXXTEST_TRACE_STACK

    #define _TS_FREE_BACKTRACE

#endif


    #define _TS_TRY_WITH_SIGNAL_PROTECTION \
        CxxTest::__cxxtest_sig_backtrace = NULL; \
        try


    #define _TS_SIGNAL_CLEANUP { \
            _TS_FREE_BACKTRACE; \
            CxxTest::__cxxtest_sigmsg = ""; \
        }


    #define _TS_PROPAGATE_SIGNAL \
        catch(CxxTest::__se_exception& e) { throw e; }


    #define _TS_CATCH_SIGNAL( action ) \
        catch(CxxTest::__se_exception& e) { \
            CxxTest::__cxxtest_se_handler(e); \
            action; \
            _TS_SIGNAL_CLEANUP \
        }


} // end namespace CxxTest

#endif // __cxxtest___SignalsWin32_h__
