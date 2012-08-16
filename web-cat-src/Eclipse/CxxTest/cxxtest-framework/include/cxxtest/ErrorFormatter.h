#ifndef __cxxtest__ErrorFormatter_h__
#define __cxxtest__ErrorFormatter_h__

//
// The ErrorFormatter is a TestListener that
// prints reports of the errors to an output
// stream.  Since we cannot rely on the standard
// iostreams, this header defines a base class
// analogout to std::ostream.
//

#include <cxxtest/TestRunner.h>
#include <cxxtest/TestListener.h>
#include <cxxtest/TestTracker.h>
#include <cxxtest/ValueTraits.h>
#include <cxxtest/OutputStream.h>

namespace CxxTest
{
    //----------------------------------------------------------------------
    class ErrorFormatter : public TestListener
    {
    public:
        // ------------------------------------------------------
        ErrorFormatter( OutputStream *o, const char *preLine = ":",
                        const char *postLine = "" ) :
            _dotting( true ),
            _reported( false ),
            _o(o),
            _preLine(preLine),
            _postLine(postLine)
        {
        }


        // ------------------------------------------------------
        int run()
        {
            TestRunner::runAllTests( *this );
            return tracker().failedTests();
        }


        // ------------------------------------------------------
        void enterWorld( const WorldDescription & /*desc*/ )
        {
            (*_o) << "Running " << totalTests;
            _o->flush();
            _dotting = true;
            _reported = false;
        }


        // ------------------------------------------------------
        static void totalTests( OutputStream &o )
        {
            char s[WorldDescription::MAX_STRLEN_TOTAL_TESTS];
            const WorldDescription &wd = tracker().world();
            o << wd.strTotalTests( s ) << (wd.numTotalTests() == 1 ? " test" : " tests");
        }


        // ------------------------------------------------------
        void enterSuite( const SuiteDescription & )
        {
            _reported = false;
        }


        // ------------------------------------------------------
        void enterTest( const TestDescription & )
        {
            _reported = false;
        }


        // ------------------------------------------------------
        void leaveTest( const TestDescription & )
        {
            if ( !tracker().testFailed() )
            {
                ((*_o) << ".").flush();
                _dotting = true;
            }
        }


        // ------------------------------------------------------
        void leaveWorld( const WorldDescription &desc )
        {
            if ( !tracker().failedTests() )
            {
                (*_o) << "OK!" << endl;
                return;
            }

            newLine();
            (*_o) << "Failed " << tracker().failedTests();
            (*_o) << " of " << totalTests << endl;

            unsigned numPassed =
                desc.numTotalTests() - tracker().failedTests();
            (*_o) << "Success rate: ";
            (*_o) << (numPassed * 100 / desc.numTotalTests()) << "%" << endl;
        }


        // ------------------------------------------------------
        void trace( const char *file, unsigned line, const char *expression )
        {
            stop( file, line ) << "Trace: " << expression << endl;
        }


        // ------------------------------------------------------
        void warning( const char *file, unsigned line, const char *expression )
        {
            stop( file, line ) << "Warning: " << expression << endl;

            walkLastBacktrace();
        }


        // ------------------------------------------------------
        void failedTest( const char *file, unsigned line,
                         const char *expression )
        {
            stop( file, line ) << "Error: Test failed: " <<
                expression << endl;
                
            walkLastBacktrace();
        }


        // ------------------------------------------------------
        void failedAssert( const char *file, unsigned line,
                           const char *expression )
        {
            stop( file, line ) << "Error: Assertion failed: " <<
                expression << endl;
        }


        // ------------------------------------------------------
        void failedAssertEquals( const char *file, unsigned line,
                                 const char *xStr, const char *yStr,
                                 const char *x, const char *y )
        {
            stop( file, line ) << "Error: Expected (" <<
                xStr << " == " << yStr << "), found (" <<
                x << " != " << y << ")" << endl;
        }


        // ------------------------------------------------------
        void failedAssertSameData( const char *file, unsigned line,
                                   const char *xStr, const char *yStr,
                                   const char *sizeStr, const void *x,
                                   const void *y, unsigned size )
        {
            stop( file, line ) << "Error: Expected " << sizeStr <<
                " (" << size << ") bytes to be equal at (" <<
                xStr << ") and (" << yStr << "), found:" << endl;
            dump( x, size );
            (*_o) << "     differs from" << endl;
            dump( y, size );
        }


        // ------------------------------------------------------
        void failedAssertDelta( const char *file, unsigned line,
                                const char *xStr, const char *yStr,
                                const char *dStr, const char *x,
                                const char *y, const char *d )
        {
            stop( file, line ) << "Error: Expected (" <<
                xStr << " == " << yStr << ") up to " << dStr <<
                " (" << d << "), found (" <<
                x << " != " << y << ")" << endl;
        }


        // ------------------------------------------------------
        void failedAssertDiffers( const char *file, unsigned line,
                                  const char *xStr, const char *yStr,
                                  const char *value )
        {
            stop( file, line ) << "Error: Expected (" <<
                xStr << " != " << yStr << "), found (" <<
                value << ")" << endl;
        }


        // ------------------------------------------------------
        void failedAssertLessThan( const char *file, unsigned line,
                                   const char *xStr, const char *yStr,
                                   const char *x, const char *y )
        {
            stop( file, line ) << "Error: Expected (" <<
                xStr << " < " << yStr << "), found (" <<
                x << " >= " << y << ")" << endl;
        }


        // ------------------------------------------------------
        void failedAssertLessThanEquals( const char *file, unsigned line,
                                         const char *xStr, const char *yStr,
                                         const char *x, const char *y )
        {
            stop( file, line ) << "Error: Expected (" <<
                xStr << " <= " << yStr << "), found (" <<
                x << " > " << y << ")" << endl;
        }


        // ------------------------------------------------------
        void failedAssertRelation( const char *file, unsigned line,
                                   const char *relation, const char *xStr,
                                   const char *yStr, const char *x,
                                   const char *y )
        {
            stop( file, line ) << "Error: Expected " << relation <<
                "( " << xStr << ", " << yStr << " ), found !" << relation <<
                "( " << x << ", " << y << " )" << endl;
        }


        // ------------------------------------------------------
        void failedAssertPredicate( const char *file, unsigned line,
                                    const char *predicate, const char *xStr,
                                    const char *x )
        {
            stop( file, line ) << "Error: Expected " << predicate << "( " <<
                xStr << " ), found !" << predicate << "( " << x << " )" <<
                endl;
        }


        // ------------------------------------------------------
        void failedAssertThrows( const char *file, unsigned line,
                                 const char *expression, const char *type,
                                 bool otherThrown )
        {
            stop( file, line ) << "Error: Expected (" << expression <<
                ") to throw (" << type << ") but it " <<
                (otherThrown ? "threw something else" : "didn't throw") <<
                endl;
        }


        // ------------------------------------------------------
        void failedAssertThrowsNot( const char *file, unsigned line,
                                    const char *expression )
        {
            stop( file, line ) << "Error: Expected (" << expression <<
                ") not to throw, but it did" << endl;
        }


    protected:
        // ------------------------------------------------------
        OutputStream *outputStream() const
        {
            return _o;
        }


    private:
        // ------------------------------------------------------
        ErrorFormatter( const ErrorFormatter & );
        

        // ------------------------------------------------------
        ErrorFormatter &operator=( const ErrorFormatter & );
        

        // ------------------------------------------------------
        OutputStream &stop( const char *file, unsigned line )
        {
            newLine();
            reportTest();

            if (line != 0 && file != 0 && *file != 0)
            	return (*_o) << file << _preLine << line << _postLine << ": ";

            return (*_o);
        }


        // ------------------------------------------------------
        void newLine( void )
        {
            if ( _dotting ) {
                (*_o) << endl;
                _dotting = false;
            }
        }


        // ------------------------------------------------------
        void reportTest( void )
        {
            if( _reported )
                return;

            (*_o) << "In " << tracker().suite().suiteName() << "::" <<
                tracker().test().testName() << ":" << endl;
            _reported = true;
        }


        // ------------------------------------------------------
        void dump( const void *buffer, unsigned size )
        {
            if ( !buffer )
                dumpNull();
            else
                dumpBuffer( buffer, size );
        }
        

        // ------------------------------------------------------
        void dumpNull()
        {
            (*_o) << "   (null)" << endl;
        }
        

        // ------------------------------------------------------
        void dumpBuffer( const void *buffer, unsigned size )
        {
            unsigned dumpSize = size;
            if ( maxDumpSize() && dumpSize > maxDumpSize() )
                dumpSize = maxDumpSize();

            const unsigned char *p = (const unsigned char *)buffer;
            (*_o) << "   { ";
            for ( unsigned i = 0; i < dumpSize; ++ i )
                (*_o) << byteToHex( *p++ ) << " ";
            if ( dumpSize < size )
                (*_o) << "... ";
            (*_o) << "}" << endl;
        }


        // ------------------------------------------------------
        bool visitBacktraceFrame(int /* index */,
                                 void* /* frame */,
                                 const char* function,
                                 const char* filename,
                                 int lineNumber)
        {
            (*_o) << "        at ";
            (*_o) << function;

            if(lineNumber)
            {
                (*_o) << " (" << filename << ":"
                    << lineNumber << ")";
            }

            (*_o) << "\n";

            if (strcmp(function, "main") == 0 ||
                strstr(function, "CxxTestMain") == function)
            {
                return false;
            }
            else
            {
                return true;
            }
        }


        // ------------------------------------------------------
        static void endl( OutputStream &o )
        {
            OutputStream::endl( o );
        }


        //~ Instance variables ...............................................

        bool _dotting;
        bool _reported;
        OutputStream *_o;
        const char *_preLine;
        const char *_postLine;
    };
};

#endif // __cxxtest__ErrorFormatter_h__
