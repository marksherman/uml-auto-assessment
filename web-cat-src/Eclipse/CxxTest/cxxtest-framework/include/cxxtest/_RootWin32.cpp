// Root signal and stack trace definitions for Win32-based systems.
// Included by Root.cpp.

namespace CxxTest
{
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
                msg = "Stack overflow (infinite recursion, "
                    "or declaration of a very large local variable?)";
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
            SafeString oldsigmsg = CxxTest::__cxxtest_sigmsg;

            CxxTest::__cxxtest_sigmsg = msg;
            CxxTest::__cxxtest_sigmsg += ", maybe related to ";
            CxxTest::__cxxtest_sigmsg += oldsigmsg;
        }

#ifdef CXXTEST_TRACE_STACK

        void* eip = (void*)e.info()->ContextRecord->Eip;
        void* ebp = (void*)e.info()->ContextRecord->Ebp;

        Dereferee::platform* platform = Dereferee::current_platform();
        CxxTest::__cxxtest_sig_backtrace = platform->get_backtrace(eip, ebp);

#endif // CXXTEST_TRACE_STACK
    }
    
} // end namespace CxxTest
