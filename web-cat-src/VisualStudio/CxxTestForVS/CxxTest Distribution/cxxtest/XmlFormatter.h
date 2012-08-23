#ifndef __CXXTEST__XMLFORMATTER_H
#define __CXXTEST__XMLFORMATTER_H

//
// The XmlFormatter is a TestListener that
// prints reports of the errors to an output
// stream in the form of an XML document.
// Since we cannot rely ou the standard
// iostreams, this header defines a base class
// analogout to std::ostream.
//

// The following definitions are used if stack trace support is enabled,
// to give the traces an easily-parsable XML format.  If stack tracing is
// not enabled, then these definitions will be ignored.
#define CXXTEST_STACK_TRACE_ESCAPE_AS_XML
#define CXXTEST_STACK_TRACE_NO_ESCAPE_FILELINE_AFFIXES

#define CXXTEST_STACK_TRACE_INITIAL_PREFIX "<stack-frame function=\""
#define CXXTEST_STACK_TRACE_INITIAL_SUFFIX "\"/>\n"
#define CXXTEST_STACK_TRACE_OTHER_PREFIX CXXTEST_STACK_TRACE_INITIAL_PREFIX
#define CXXTEST_STACK_TRACE_OTHER_SUFFIX CXXTEST_STACK_TRACE_INITIAL_SUFFIX
#define CXXTEST_STACK_TRACE_ELLIDED_MESSAGE ""
#define CXXTEST_STACK_TRACE_FILELINE_PREFIX "\" location=\""
#define CXXTEST_STACK_TRACE_FILELINE_SUFFIX ""


#include <cxxtest/TestRunner.h>
#include <cxxtest/TestListener.h>
#include <cxxtest/TestTracker.h>
#include <cxxtest/ValueTraits.h>
#include <string>

namespace CxxTest
{
    class OutputStream
    {
    public:
        virtual ~OutputStream() {}
        virtual void flush() {};
        virtual OutputStream &operator<<( unsigned /*number*/ ) { return *this; }
        virtual OutputStream &operator<<( const char * /*string*/ ) { return *this; }

        typedef void (*Manipulator)( OutputStream & );
        
        virtual OutputStream &operator<<( Manipulator m ) { m( *this ); return *this; }
        static void endl( OutputStream &o ) { (o << "\n").flush(); }
    };

    class XmlFormatter : public TestListener
    {
    public:
        XmlFormatter( OutputStream *o ) : _o(o) { }

        int run()
        {
        		(*_o) << "<?xml version='1.0'?>" << endl;
        		_o->flush();

            TestRunner::runAllTests( *this );
            return tracker().failedTests();
        }

        void enterWorld( const WorldDescription & /*desc*/ )
        {
            (*_o) << "<world>" << endl;
            _o->flush();
        }

        static void totalTests( OutputStream &o )
        {
            char s[WorldDescription::MAX_STRLEN_TOTAL_TESTS];
            const WorldDescription &wd = tracker().world();
            o << wd.strTotalTests( s ) << (wd.numTotalTests() == 1 ? " test" : " tests");
        }

        void enterSuite( const SuiteDescription& desc )
        {
        		(*_o) << "    <suite name=\"" << desc.suiteName() << "\" ";
        		(*_o) << "file=\"" << desc.file() << "\" ";
        		(*_o) << "line=\"" << desc.line() << "\" ";
        		(*_o) << ">"<< endl;
        		_o->flush();
        }

        void leaveSuite( const SuiteDescription & )
        {
        		(*_o) << "    </suite>" << endl;
        		_o->flush();
        }

        void enterTest( const TestDescription & desc )
        {
        		(*_o) << "        <test name=\"" << desc.testName() << "\" ";
        		(*_o) << "line=\"" << desc.line() << "\" ";
        		(*_o) << ">" << endl;
        		_o->flush();
        }

        void leaveTest( const TestDescription & )
        {
        		(*_o) << "        </test>" << endl;
        		_o->flush();

#ifdef CXXTEST_CREATE_BINARY_LOG
        		executionLog.commitLastResult();
#endif
        }

        void leaveWorld( const WorldDescription &desc )
        {
        		(*_o) << "</world>" << endl;
        		_o->flush();
        }

        void trace( const char *file, unsigned line, const char *expression )
        {
            startTag( "trace", file, line );
            attribute( "message", expression );
            endTag();
        }

        void suiteInitError( const char *file, unsigned line, const char *expression )
        {
            (*_o) << "        <suite-error type=\"init\" line=\"" << line << "\">" << endl;
            (*_o) << expression;
            (*_o) << "        </suite-error>" << endl;
            _o->flush();
        }

        void warning( const char *file, unsigned line, const char *expression )
        {
            (*_o) << "            <warning line=\"" << line << "\">" << endl;
            (*_o) << expression;
            (*_o) << "            </warning>" << endl;
            _o->flush();
        }

        void failedTest( const char *file, unsigned line, const char *expression )
        {
            (*_o) << "            <failed-test line=\"" << line << "\">" << endl;
            (*_o) << expression;
            (*_o) << "            </failed-test>" << endl;
            _o->flush();

#ifdef CXXTEST_CREATE_BINARY_LOG
			if(!executionLog.isLastResultSet())
        		executionLog.setLastResult(LOG_TEST_FAILED_TEST, expression);
#endif
        }

        void failedAssert( const char *file, unsigned line, const char *expression )
        {
            startTag( "failed-assert", file, line );
            attribute( "expression", expression );
            endTag();

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

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

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

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

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

        void failedAssertDelta( const char *file, unsigned line,
                                const char *xStr, const char *yStr, const char *dStr,
                                const char *x, const char *y, const char *d )
        {
            startTag( "failed-assert-delta", file, line );
            attribute( "lhs-desc", xStr );
            attribute( "lhs-value", x );
            attribute( "rhs-desc", yStr );
            attribute( "rhs-value", y );
            attribute( "delta-desc", dStr );
            attribute( "delta-value", d );
            endTag();

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

        void failedAssertDiffers( const char *file, unsigned line,
                                  const char *xStr, const char *yStr,
                                  const char *value )
        {
            startTag( "failed-assert-ne", file, line );
            attribute( "lhs-desc", xStr );
            attribute( "rhs-desc", yStr );
            attribute( "value", value );
            endTag();

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

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

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

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

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

        void failedAssertRelation( const char *file, unsigned line,
                                   const char *relation, const char *xStr, const char *yStr,
                                   const char *x, const char *y )
        {
            startTag( "failed-assert-relation", file, line );
            attribute( "relation", relation );
            attribute( "lhs-desc", xStr );
            attribute( "lhs-value", x );
            attribute( "rhs-desc", yStr );
            attribute( "rhs-value", y );
            endTag();

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

        void failedAssertPredicate( const char *file, unsigned line,
                                    const char *predicate, const char *xStr, const char *x )
        {
            startTag( "failed-assert-predicate", file, line );
            attribute( "predicate", predicate );
            attribute( "arg-desc", xStr );
            attribute( "arg-value", x );
            endTag();

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

        void failedAssertThrows( const char *file, unsigned line,
                                 const char *expression, const char *type,
                                 bool otherThrown )
        {
            startTag( "failed-assert-throws", file, line );
            attribute( "expression", expression );
            attribute( "type", type );
            attribute( "threw", otherThrown ? "other" : "none" );
            endTag();

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

        void failedAssertThrowsNot( const char *file, unsigned line, const char *expression )
        {
            startTag( "failed-assert-nothrow", file, line );
            attribute( "expression", expression );
            endTag();

#ifdef CXXTEST_CREATE_BINARY_LOG
        	executionLog.setLastResult(LOG_TEST_FAILED_ASSERT);
#endif
        }

    protected:
        OutputStream *outputStream() const
        {
            return _o;
        }

    private:
        XmlFormatter( const XmlFormatter & );
        XmlFormatter &operator=( const XmlFormatter & );

		std::string escape(const std::string& str)
		{
			std::string escStr = "";
			for(size_t i = 0; i < str.length(); i++)
			{
				switch(str[i])
				{
					case '"':  escStr += "&quot;"; break;
					case '\'': escStr += "&apos;"; break;
					case '<':  escStr += "&lt;"; break;
					case '>':  escStr += "&gt;"; break;
					case '&':  escStr += "&amp;"; break;
					default:   escStr += str[i]; break;
				}
			}
			
			return escStr;
		}
		        
        OutputStream &startTag( const char* name, const char *file, unsigned line )
        {
            (*_o) << "            <" << name;
            (*_o) << " line=\"" << line << "\" ";
            return (*_o);
        }

        OutputStream &attribute( const char* name, const char *value )
        {
            (*_o) << name;
            (*_o) << "=\"" << escape(value).c_str() << "\" ";
            return (*_o);
        }

        OutputStream &attribute( const char* name, unsigned value )
        {
            (*_o) << name;
            (*_o) << "=\"" << value << "\" ";
            return (*_o);
        }

        OutputStream &attributeBinary( const char* name, const void *value, unsigned size )
        {
            (*_o) << name;
            (*_o) << "=\"";
            dump(value, size);
            (*_o) << "\" ";
            return (*_o);
        }

        OutputStream &endTag()
        {
            (*_o) << "/>" << endl;
            _o->flush();
            return (*_o);
        }

        void dump( const void *buffer, unsigned size )
        {
            unsigned dumpSize = size;
            if ( maxDumpSize() && dumpSize > maxDumpSize() )
                dumpSize = maxDumpSize();

            const unsigned char *p = (const unsigned char *)buffer;
            for ( unsigned i = 0; i < dumpSize; ++ i )
                (*_o) << byteToHex( *p++ ) << " ";
            if ( dumpSize < size )
                (*_o) << "... ";
        }

        static void endl( OutputStream &o )
        {
            OutputStream::endl( o );
        }

        OutputStream *_o;
    };
};

#endif // __CXXTEST__ERRORFORMATTER_H
