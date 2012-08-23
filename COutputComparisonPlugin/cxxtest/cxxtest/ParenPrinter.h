#ifndef __CXXTEST__PARENPRINTER_H
#define __CXXTEST__PARENPRINTER_H

//
// The ParenPrinter is identical to the ErrorPrinter, except it
// prints the line number in a format expected by some compilers
// (notably, MSVC).
//

#include <cxxtest/ErrorPrinter.h>

namespace CxxTest 
{
    class ParenPrinter : public ErrorPrinter
    {
    public:
        ParenPrinter( CXXTEST_STD(ostream) &o = CXXTEST_STD(cout) ) : ErrorPrinter( o, "(", ")" ) {}
    };
}

#endif // __CXXTEST__PARENPRINTER_H
