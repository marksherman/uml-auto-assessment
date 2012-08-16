#ifndef __cxxtest__StdioFilePrinter_h__
#define __cxxtest__StdioFilePrinter_h__

//
// The StdioFilePrinter is a simple TestListener that just prints "OK" if
// everything goes well, otherwise reports the error in the format of
// compiler messages. This class uses <stdio.h>, i.e. FILE* and fprintf().
//

#include <cxxtest/ErrorFormatter.h>
#include <cxxtest/OutputStream.h>
#include <stdio.h>

namespace CxxTest 
{
    //----------------------------------------------------------------------
    class StdioFilePrinter : public ErrorFormatter
    {
    public:
        // ------------------------------------------------------
        StdioFilePrinter( FILE *o, const char *preLine = ":",
                          const char *postLine = "" ) :
            ErrorFormatter( new FileOutputStream(o), preLine, postLine )
        {
        }


        // ------------------------------------------------------
        virtual ~StdioFilePrinter()
        {
            delete outputStream();
        }
    };
}

#endif // __cxxtest__StdioFilePrinter_h__
