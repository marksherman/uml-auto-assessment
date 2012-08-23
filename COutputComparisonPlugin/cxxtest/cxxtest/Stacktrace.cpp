extern "C" {
    void __cyg_profile_func_enter( void *, void * ) _CXXTEST_NO_INSTR;
    void __cyg_profile_func_exit( void *, void * ) _CXXTEST_NO_INSTR;
}

#ifndef CXXTEST_TRACE_STACK

extern "C" {
    void __cyg_profile_func_enter( void *, void * ) {}
    void __cyg_profile_func_exit( void *, void * ) {}
}

#else

#define CXXTEST_PLAIN_STACK_TRACE_INITIAL_PREFIX "  in: "
#define CXXTEST_PLAIN_STACK_TRACE_INITIAL_SUFFIX "\n"
#define CXXTEST_PLAIN_STACK_TRACE_OTHER_PREFIX "    called from: "
#define CXXTEST_PLAIN_STACK_TRACE_OTHER_SUFFIX "\n"
#define CXXTEST_PLAIN_STACK_TRACE_ELLIDED_MESSAGE "    ...\n"
#define CXXTEST_PLAIN_STACK_TRACE_FILELINE_PREFIX "("
#define CXXTEST_PLAIN_STACK_TRACE_FILELINE_SUFFIX ")"

#ifndef CXXTEST_STACK_TRACE_INITIAL_PREFIX
#   define CXXTEST_STACK_TRACE_INITIAL_PREFIX "  in: "
#endif
#ifndef CXXTEST_STACK_TRACE_INITIAL_SUFFIX
#   define CXXTEST_STACK_TRACE_INITIAL_SUFFIX "\n"
#endif
#ifndef CXXTEST_STACK_TRACE_OTHER_PREFIX
#   define CXXTEST_STACK_TRACE_OTHER_PREFIX "    called from: "
#endif
#ifndef CXXTEST_STACK_TRACE_OTHER_SUFFIX
#   define CXXTEST_STACK_TRACE_OTHER_SUFFIX "\n"
#endif
#ifndef CXXTEST_STACK_TRACE_ELLIDED_MESSAGE
#   define CXXTEST_STACK_TRACE_ELLIDED_MESSAGE "    ...\n"
#endif
#ifndef CXXTEST_STACK_TRACE_FILELINE_PREFIX
#   define CXXTEST_STACK_TRACE_FILELINE_PREFIX "("
#endif
#ifndef CXXTEST_STACK_TRACE_FILELINE_SUFFIX
#   define CXXTEST_STACK_TRACE_FILELINE_SUFFIX ")"
#endif
#ifndef CXXTEST_STACK_TRACE_MAX_FRAMES_TO_DUMP
#   define CXXTEST_STACK_TRACE_MAX_FRAMES_TO_DUMP 12
#endif

#include <string.h>
#include <symreader.h>
#include <sstream>

#define _CXXTEST_MAXIMUM_STACK_TRACE_DEPTH 4096

namespace CxxTest {

struct StackElem
{
    void* func;
    void* callsite;
};

struct SymbolInfo
{
	void* pc;
    char  fileName[PATH_MAX];
    char  funcName[SYMNAME_MAX];
    int line;
    bool         found;
};

static StackElem __cxxtest_stack[_CXXTEST_MAXIMUM_STACK_TRACE_DEPTH];
unsigned int __cxxtest_stackTop = 0;
static bool __cxxtest_generatingTrace  = false;
bool __cxxtest_handlingOverflow = false;

// Function prototypes to enforce no-instrument attributes
static bool printStackTraceEntry(
    bool, std::ostream&, void*, const char*, const char* ) _CXXTEST_NO_INSTR;
bool getCallerInfo(std::string& name, std::string& filename, int& line)
	_CXXTEST_NO_INSTR;
std::string getStackTrace(
    bool forcePlainText        = false,
    unsigned int top           = __cxxtest_stackTop,
    StackElem*   stackBase     = __cxxtest_stack,
    const char* initialPrefix  = 0,
    const char* otherPrefix    = 0
    ) _CXXTEST_NO_INSTR;
static bool shouldPrintEntry( const std::string& ) _CXXTEST_NO_INSTR;
bool findInStackTrace(bool (*matcher)(const std::string&)) _CXXTEST_NO_INSTR;
unsigned int stackTraceSize( unsigned int traceDepth ) _CXXTEST_NO_INSTR;
void saveStackTraceWindow( StackElem* dest, unsigned int traceDepth )
  _CXXTEST_NO_INSTR;


// -------------------------------------------------------------------------
bool getCallerInfo(std::string& name, std::string& filename, int& line)
{
	unsigned int top = __cxxtest_stackTop; 
    while ( top && !__cxxtest_stack[top - 1].func ) top--;

    if (top)
    {
		void* caller = __cxxtest_stack[__cxxtest_stackTop - 1].func;
	
	    if (caller)
	    {
			char symNameBuf[SYMNAME_MAX];
			char filenameBuf[PATH_MAX];
			int lineNum;
	
		    void* found = symreader_get_symbol_info(caller,
		    	symNameBuf, filenameBuf, &lineNum);
		    	
		    if(found)
		    {
		    	name = symNameBuf;
		    	filename = filenameBuf;
		    	line = lineNum;
		    	return true;
		    }
	    }
    }
    
    return false;
}

std::string getStackTrace(
    bool forcePlainText,
    unsigned int top,
    StackElem* stackBase,
    const char* initialPrefix,
    const char* otherPrefix )
{
    if(!initialPrefix)
    	initialPrefix = forcePlainText?
    		CXXTEST_PLAIN_STACK_TRACE_INITIAL_PREFIX :
    		CXXTEST_STACK_TRACE_INITIAL_PREFIX;

    if(!otherPrefix)
    	otherPrefix   = forcePlainText?
    		CXXTEST_PLAIN_STACK_TRACE_OTHER_PREFIX :
    		CXXTEST_STACK_TRACE_OTHER_PREFIX;

	if(symreader_is_initialized() == 0)
	{
		char* envPath = getenv("CXXTEST_EXE_PATH");
		symreader_initialize(envPath, SYMFLAGS_DEMANGLE);
	}
	
    __cxxtest_generatingTrace = true;
    std::ostringstream result;
    while ( top && !stackBase[top - 1].func ) top--;
    if ( top )
    {
        // result << " (" << top << ") ";
        printStackTraceEntry(
            forcePlainText,
            result,
            stackBase[top - 1].func,
            initialPrefix,
            forcePlainText?
            	CXXTEST_PLAIN_STACK_TRACE_INITIAL_SUFFIX : CXXTEST_STACK_TRACE_INITIAL_SUFFIX );
        unsigned int printedCount = 0;
        for ( unsigned int i = top; i > 0; )
        {
            i--;
            // result << " (" << i << ") ";
            if ( printStackTraceEntry(
                forcePlainText,
                result,
                stackBase[i].callsite,
                otherPrefix,
                forcePlainText?
                	CXXTEST_PLAIN_STACK_TRACE_OTHER_SUFFIX : CXXTEST_STACK_TRACE_OTHER_SUFFIX ) ) printedCount++;
            if ( printedCount > CXXTEST_STACK_TRACE_MAX_FRAMES_TO_DUMP )
            {
                result <<
                	(forcePlainText? CXXTEST_PLAIN_STACK_TRACE_ELLIDED_MESSAGE : CXXTEST_STACK_TRACE_ELLIDED_MESSAGE);
		break;
            }
        }
    }
    __cxxtest_generatingTrace = false;
    return result.str();
}

bool findInStackTrace(bool (*matcher)(const std::string&), std::string& symbolFound)
{
    unsigned int top           = __cxxtest_stackTop;
    StackElem*   stackBase     = __cxxtest_stack;

    __cxxtest_generatingTrace = true;
	if(symreader_is_initialized() == 0)
	{
		char* envPath = getenv("CXXTEST_EXE_PATH");
		symreader_initialize(envPath, SYMFLAGS_DEMANGLE);
	}

	char symName[SYMNAME_MAX];
	char filename[PATH_MAX];
	int line;
	
    while ( top && !stackBase[top - 1].func ) top--;
    if ( top )
    {
	    void* found = symreader_get_symbol_info(stackBase[top - 1].func,
    		symName, filename, &line);
    	
	    if(found)
    	{
			std::string symbol = symName;

    		if((*matcher)(symbol))
    		{
			    __cxxtest_generatingTrace = false;
			    symbolFound = symbol;
    			return true;
    		}
    	}
    	
        for ( unsigned int i = top; i > 0; )
        {
            i--;

		    void* found = symreader_get_symbol_info(stackBase[i].callsite,
	    		symName, filename, &line);
	    	
		    if(found)
	    	{
				std::string symbol = symName;

	    		if((*matcher)(symbol))
	    		{
				    __cxxtest_generatingTrace = false;
				    symbolFound = symbol;
	    			return true;
	    		}
	    	}
        }
    }
    
    __cxxtest_generatingTrace = false;
    return false;
}

// -------------------------------------------------------------------------
#ifndef CXXTEST_STACK_TRACE_ESCAPE_AS_XML
#   define escape
#else
#   define escape(x) escapeString(x)
#endif // CXXTEST_STACK_TRACE_ESCAPE_AS_XML
		
// -------------------------------------------------------------------------
static bool printStackTraceEntry(
	bool forcePlainText,
    std::ostream& dest,
    void*         location,
    const char*   prefix,
    const char*   suffix
    )
{
    if ( !location ) return false;

	char symName[SYMNAME_MAX] = { 0 };
	char filename[PATH_MAX] = { 0 };
	int line = 0;

    void* found = symreader_get_symbol_info(location,
    	symName, filename, &line);
    	
    if(found)
    {
		std::string symbol = symName;
        if(shouldPrintEntry(symbol))
        {
            if(symbol == "__assert" || symbol == "__eprintf")
            {
                // translate asserts
                symbol = "assert(bool) macro";
                filename[0] = '\0';
            }
            else if(symbol.find( "operator new" ) == 0
				|| symbol.find( "operator delete" ) == 0 )
            {
                filename[0] = '\0';
            }
			else if(symbol.find( "__GLOBAL__D_" ) == 0)
			{
				symbol = "destruction of global variable '" +
					symbol.substr(12) + "'";
				line = 0;
			}
			else if(symbol.find( "__GLOBAL__I_" ) == 0)
			{
				symbol = "construction of global variable '" +
					symbol.substr(12) + "'";
				line = 0;
			}

            dest << prefix << (forcePlainText? symbol : escape(symbol));

//            if(line)
            {
                const char* dir = "Debug/../";
                const char* fname = strstr(filename, dir);

                if(fname)
                {
                    fname += strlen(dir);
                }
                else
                {
                    fname = filename;
                    const char* dir2 = "cxxtest/cxxtest/";
                    fname = strstr(fname, dir2);
                    if(fname)
                    {
                        fname += strlen(dir2) / 2;
                    }
                    else
                    {
                        fname = filename;
                    }
                }
                if(fname && fname[0])
                {
                	const char* fileLinePrefix = forcePlainText?
                		CXXTEST_PLAIN_STACK_TRACE_FILELINE_PREFIX :
                		CXXTEST_STACK_TRACE_FILELINE_PREFIX;
                	const char* fileLineSuffix = forcePlainText?
                		CXXTEST_PLAIN_STACK_TRACE_FILELINE_SUFFIX :
                		CXXTEST_STACK_TRACE_FILELINE_SUFFIX;

#ifdef CXXTEST_STACK_TRACE_NO_ESCAPE_FILELINE_AFFIXES
                    dest << fileLinePrefix << (forcePlainText? fname : escape(fname));
#else
                    dest << (forcePlainText? fileLinePrefix : escape(fileLinePrefix)) <<
                    	(forcePlainText? fname : escape(fname));
#endif
                    if(line)
                    {
                        dest << ":" << line;
                    }

#ifdef CXXTEST_STACK_TRACE_NO_ESCAPE_FILELINE_AFFIXES
                    dest << fileLineSuffix;
#else
                    dest << (forcePlainText? fileLineSuffix : escape(fileLineSuffix));
#endif
                }
            }

            dest << suffix;
            return true;
        }
    }

    return false;
}


// -------------------------------------------------------------------------
// This is a filtering function to trim unnecessary clutter from
// the stack trace
static bool shouldPrintEntry( const std::string& funcName )
{
	if(funcName.find( "__GLOBAL__" ) == 0)
	{
		return true;
	}

    return funcName.length()
           && funcName.find( "CxxTest::" ) == std::string::npos
           && funcName.find( "ChkPtr::" ) == std::string::npos
           && funcName.find( "TestDescription_" ) != 0
           && funcName.find( "Ptr<" ) != 0
           && funcName.find( "__" ) != 0
           && funcName.find( "Memwatch::" ) == std::string::npos
           ;
}


// -------------------------------------------------------------------------
unsigned int stackTraceSize( unsigned int traceDepth )
{
    return traceDepth * sizeof( StackElem );
}


// -------------------------------------------------------------------------
void saveStackTraceWindow( StackElem* dest, unsigned int traceDepth )
{
    unsigned int offset = traceDepth;
    if ( offset > __cxxtest_stackTop ) offset = __cxxtest_stackTop;
    offset = __cxxtest_stackTop - offset;

    for ( unsigned int i = 0; i < traceDepth; i++ )
    {
        unsigned int pos = offset + i;
        if ( pos < __cxxtest_stackTop )
        {
            dest[i] = __cxxtest_stack[offset + i];
        }
        else
        {
            dest[i].func     = 0;
            dest[i].callsite = 0;
        }
    }
}


};  // End of namespace CxxTest


// -------------------------------------------------------------------------
void __cyg_profile_func_enter( void* func, void* callsite )
{
    if ( CxxTest::__cxxtest_generatingTrace ) return;
    if ( CxxTest::__cxxtest_handlingOverflow ) return;
    if ( CxxTest::__cxxtest_stackTop == _CXXTEST_MAXIMUM_STACK_TRACE_DEPTH )
    {
        CxxTest::__cxxtest_handlingOverflow = true;
#ifdef CXXTEST_TRAP_SIGNALS
        CxxTest::__cxxtest_assertmsg = HINT_PREFIX "call stack too deep";
#endif
        abort();
    }

    CxxTest::__cxxtest_stack[CxxTest::__cxxtest_stackTop].func = func;
    CxxTest::__cxxtest_stack[CxxTest::__cxxtest_stackTop].callsite = callsite;
    CxxTest::__cxxtest_stackTop++;
}


// -------------------------------------------------------------------------
void __cyg_profile_func_exit( void*, void* )
{
    if ( CxxTest::__cxxtest_generatingTrace ) return;
    if ( CxxTest::__cxxtest_handlingOverflow ) return;
    if ( CxxTest::__cxxtest_stackTop )
    {
        CxxTest::__cxxtest_stackTop--;
    }
}

#endif
