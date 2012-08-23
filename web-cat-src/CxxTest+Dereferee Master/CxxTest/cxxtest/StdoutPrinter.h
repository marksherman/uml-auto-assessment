#ifndef __cxxtest__StdoutPrinter_h__
#define __cxxtest__StdoutPrinter_h__

//
// The StdoutPrinter is an StdioFilePrinter which defaults to stdout.
//

#include <cxxtest/StdioFilePrinter.h>

namespace CxxTest 
{
    //----------------------------------------------------------------------
    class StdoutPrinter : public StdioFilePrinter
    {
    public:
        // ------------------------------------------------------
        StdoutPrinter( FILE *o = stdout, const char *preLine = ":",
                      const char *postLine = "" ) :
            StdioFilePrinter( o, preLine, postLine )
        {
        }
    };


    //
    // Support the legacy class name.
    //
    typedef StdoutPrinter StdioPrinter;
 
} // end namespace CxxTest

#endif // __cxxtest__StdoutPrinter_h__
