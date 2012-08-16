#ifndef __cxxtest__XmlFormatter_h__
#define __cxxtest__XmlFormatter_h__

//
// The XmlFormatter is a TestListener that prints reports of the errors to an
// output stream in the form of an XML document. Since we cannot rely on the
// standard iostreams, this header defines a base class analogous to
// std::ostream.
//

#include <cxxtest/TestRunner.h>
#include <cxxtest/TestListener.h>
#include <cxxtest/TestTracker.h>
#include <cxxtest/ValueTraits.h>
#include <cxxtest/SafeString.h>
#include <cxxtest/OutputStream.h>

namespace CxxTest
{
    //----------------------------------------------------------------------
    class XmlFormatter : public TestListener
    {
    public:
        // ------------------------------------------------------
        XmlFormatter( OutputStream *o ) : _o(o) { }


        // ------------------------------------------------------
        int run()
        {
            (*_o) << "<?xml version='1.0'?>" << endl;
            _o->flush();

            TestRunner::runAllTests( *this );
            return tracker().failedTests();
        }


        // ------------------------------------------------------
        void enterWorld( const WorldDescription & /*desc*/ )
        {
            (*_o) << "<world>" << endl;
            _o->flush();
        }


        // ------------------------------------------------------
        static void totalTests( OutputStream &o )
        {
            char s[WorldDescription::MAX_STRLEN_TOTAL_TESTS];
            const WorldDescription &wd = tracker().world();
            o << wd.strTotalTests( s );
            o << (wd.numTotalTests() == 1 ? " test" : " tests");
        }


        // ------------------------------------------------------
        void enterSuite( const SuiteDescription& desc )
        {
            (*_o) << "    <suite name=\"";
            (*_o) << escape(desc.suiteName()) << "\" ";
            (*_o) << "file=\"" << escape(desc.file()) << "\" ";
            (*_o) << "line=\"" << desc.line() << "\" ";
            (*_o) << ">"<< endl;
            _o->flush();
        }


        // ------------------------------------------------------
        void leaveSuite( const SuiteDescription & )
        {
            (*_o) << "    </suite>" << endl;
            _o->flush();
        }


        // ------------------------------------------------------
        void enterTest( const TestDescription & desc )
        {
            (*_o) << "        <test name=\"";
            (*_o) << escape(desc.testName()) << "\" ";
            (*_o) << "line=\"" << desc.line() << "\" ";
            (*_o) << ">" << endl;
            _o->flush();
        }


        // ------------------------------------------------------
        void leaveTest( const TestDescription & )
        {
            (*_o) << "        </test>" << endl;
            _o->flush();
        }


        // ------------------------------------------------------
        void leaveWorld( const WorldDescription &desc )
        {
            (*_o) << "</world>" << endl;
            _o->flush();
        }


        // ------------------------------------------------------
        void trace( const char *file, unsigned line, const char *expression )
        {
            startTag( "trace", file, line );
            attribute( "message", expression );
            endTag();
        }


        // ------------------------------------------------------
        void suiteInitError( const char *file, unsigned line,
                             const char *expression )
        {
            (*_o) << "        <suite-error type=\"init\"";
            (*_o) << "line=\"" << line << "\">" << endl;
            (*_o) << escape(expression);
            walkLastBacktrace();
            (*_o) << "        </suite-error>" << endl;
            _o->flush();
        }


        // ------------------------------------------------------
        void warning( const char *file, unsigned line,
                      const char *expression )
        {
            (*_o) << "            <warning line=\"";
            (*_o) << line << "\">" << endl;
            (*_o) << escape(expression) << endl;
            walkLastBacktrace();
            (*_o) << "            </warning>" << endl;
            _o->flush();
        }


        // ------------------------------------------------------
        void failedTest( const char *file, unsigned line,
                         const char *expression )
        {
            (*_o) << "            <failed-test line=\"";
            (*_o) << line << "\">" << endl;
            (*_o) << escape(expression) << endl;
            walkLastBacktrace();
            (*_o) << "            </failed-test>" << endl;
            _o->flush();
        }


        // ------------------------------------------------------
        void failedAssert( const char *file, unsigned line,
                           const char *expression )
        {
            startTag( "failed-assert", file, line );
            attribute( "expression", expression );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertEquals( const char *file, unsigned line,
                                 const char *xStr, const char *yStr,
                                 const char *x, const char *y )
        {
            startTag( "failed-assert-eq", file, line );
            attribute( "lhs-desc", xStr );
            attribute( "lhs-value", x );
            attribute( "rhs-desc", yStr );
            attribute( "rhs-value", y );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertSameData( const char *file, unsigned line,
                                   const char *xStr, const char *yStr,
                                   const char *sizeStr, const void *x,
                                   const void *y, unsigned size )
        {
            startTag( "failed-assert-same-data", file, line );
            attribute( "lhs-desc", xStr );
            attributeBinary( "lhs-value", x, size );
            attribute( "rhs-desc", yStr );
            attributeBinary( "rhs-value", y, size );
            attribute( "size-desc", sizeStr );
            attribute( "size-value", size );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertDelta( const char *file, unsigned line,
                                const char *xStr, const char *yStr,
                                const char *dStr, const char *x,
                                const char *y, const char *d )
        {
            startTag( "failed-assert-delta", file, line );
            attribute( "lhs-desc", xStr );
            attribute( "lhs-value", x );
            attribute( "rhs-desc", yStr );
            attribute( "rhs-value", y );
            attribute( "delta-desc", dStr );
            attribute( "delta-value", d );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertDiffers( const char *file, unsigned line,
                                  const char *xStr, const char *yStr,
                                  const char *value )
        {
            startTag( "failed-assert-ne", file, line );
            attribute( "lhs-desc", xStr );
            attribute( "rhs-desc", yStr );
            attribute( "value", value );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertLessThan( const char *file, unsigned line,
                                   const char *xStr, const char *yStr,
                                   const char *x, const char *y )
        {
            startTag( "failed-assert-lt", file, line );
            attribute( "lhs-desc", xStr );
            attribute( "lhs-value", x );
            attribute( "rhs-desc", yStr );
            attribute( "rhs-value", y );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertLessThanEquals( const char *file, unsigned line,
                                         const char *xStr, const char *yStr,
                                         const char *x, const char *y )
        {
            startTag( "failed-assert-le", file, line );
            attribute( "lhs-desc", xStr );
            attribute( "lhs-value", x );
            attribute( "rhs-desc", yStr );
            attribute( "rhs-value", y );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertRelation( const char *file, unsigned line,
                                   const char *relation, const char *xStr,
                                   const char *yStr, const char *x,
                                   const char *y )
        {
            startTag( "failed-assert-relation", file, line );
            attribute( "relation", relation );
            attribute( "lhs-desc", xStr );
            attribute( "lhs-value", x );
            attribute( "rhs-desc", yStr );
            attribute( "rhs-value", y );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertPredicate( const char *file, unsigned line,
                                    const char *predicate, const char *xStr,
                                    const char *x )
        {
            startTag( "failed-assert-predicate", file, line );
            attribute( "predicate", predicate );
            attribute( "arg-desc", xStr );
            attribute( "arg-value", x );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertThrows( const char *file, unsigned line,
                                 const char *expression, const char *type,
                                 bool otherThrown )
        {
            startTag( "failed-assert-throws", file, line );
            attribute( "expression", expression );
            attribute( "type", type );
            attribute( "threw", otherThrown ? "other" : "none" );
            endTag();
        }


        // ------------------------------------------------------
        void failedAssertThrowsNot( const char *file, unsigned line,
                                    const char *expression )
        {
            startTag( "failed-assert-nothrow", file, line );
            attribute( "expression", expression );
            endTag();
        }


    protected:
        // ------------------------------------------------------
        OutputStream* outputStream() const
        {
            return _o;
        }


    private:
        // ------------------------------------------------------
        XmlFormatter( const XmlFormatter& );


        // ------------------------------------------------------
        XmlFormatter& operator=( const XmlFormatter& );


        // ------------------------------------------------------
        SafeString escape(const char* str)
        {
            SafeString escStr;
            while(*str)
            {
                switch(*str)
                {
                    case '"':  escStr += "&quot;"; break;
                    case '\'': escStr += "&apos;"; break;
                    case '<':  escStr += "&lt;"; break;
                    case '>':  escStr += "&gt;"; break;
                    case '&':  escStr += "&amp;"; break;
                    default:   escStr += *str; break;
                }
                
                str++;
            }
            
            return escStr;
        }
                

        // ------------------------------------------------------
        OutputStream& startTag( const char* name, const char *file,
                                unsigned line )
        {
            (*_o) << "            <" << name;
            (*_o) << " line=\"" << line << "\" ";
            return (*_o);
        }


        // ------------------------------------------------------
        OutputStream& attribute( const char* name, const char *value )
        {
            (*_o) << name;
            (*_o) << "=\"" << escape(value).c_str() << "\" ";
            return (*_o);
        }


        // ------------------------------------------------------
        OutputStream& attribute( const char* name, unsigned value )
        {
            (*_o) << name;
            (*_o) << "=\"" << value << "\" ";
            return (*_o);
        }


        // ------------------------------------------------------
        OutputStream& attributeBinary( const char* name, const void *value,
                                       unsigned size )
        {
            (*_o) << name;
            (*_o) << "=\"";
            dump(value, size);
            (*_o) << "\" ";
            return (*_o);
        }


        // ------------------------------------------------------
        OutputStream& endTag()
        {
            (*_o) << "/>" << endl;
            _o->flush();
            return (*_o);
        }


        // ------------------------------------------------------
        bool visitBacktraceFrame(int index, void* frame, const char* function,
                                 const char* filename, int lineNumber)
        {
            (*_o) << "<stack-frame function=\"";
            (*_o) << escape(function).c_str();
            (*_o) << "\" ";

            if(lineNumber)
            {
                (*_o) << "location=\"";
                (*_o) << escape(filename).c_str();
                (*_o) << ":";
                (*_o) << lineNumber;
                (*_o) << "\" ";
            }

            (*_o) << "/>\n";

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
            (*_o) << "(null)";
        }
        

        // ------------------------------------------------------
        void dumpBuffer( const void *buffer, unsigned size )
        {
            unsigned dumpSize = size;
            if ( maxDumpSize() && dumpSize > maxDumpSize() )
            {
                dumpSize = maxDumpSize();
            }

            const unsigned char *p = (const unsigned char *)buffer;
            for ( unsigned i = 0; i < dumpSize; ++ i )
            {
                (*_o) << byteToHex( *p++ ) << " ";
            }
            
            if ( dumpSize < size )
            {
                (*_o) << "... ";
            }
        }


        // ------------------------------------------------------
        static void endl( OutputStream &o )
        {
            OutputStream::endl( o );
        }


        //~ Static/instance variables ........................................

        OutputStream *_o;
    };
};

#endif // __cxxtest__XmlFormatter_h__
