#ifndef __CXXTEST__STDIOPRINTER_H
#define __CXXTEST__STDIOPRINTER_H

//
// The StdioPrinter is an StdioFilePrinter which defaults to stdout.
// This should have been called StdOutPrinter or something, but the name
// has been historically used.
//

#include <cxxtest/StdioFilePrinter.h>

namespace CxxTest 
{
    class StdioPrinter : public StdioFilePrinter
    {
    public:
        StdioPrinter( FILE *o = stdout, const char *preLine = ":", const char *postLine = "" ) :
            StdioFilePrinter( o, preLine, postLine ) {}
    };
}

#endif // __CXXTEST__STDIOPRINTER_H
