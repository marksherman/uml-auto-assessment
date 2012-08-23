#ifndef __cxxtest__XmlStdioPrinter_h__
#define __cxxtest__XmlStdioPrinter_h__

//
// The XmlStdioPrinter is an XmlFormatter which defaults to stdout.
//

#include <cxxtest/XmlFormatter.h>
#include <cxxtest/OutputStream.h>
#include <stdio.h>

namespace CxxTest 
{
    //----------------------------------------------------------------------
    class XmlStdioPrinter : public XmlFormatter
    {
    public:
        // ------------------------------------------------------
        XmlStdioPrinter() :
            XmlFormatter( new FileOutputStream(
                fopen(".cxxtest.log", "w"), true ))
        {
        }
        
        
        // ------------------------------------------------------
        XmlStdioPrinter(FILE* file) :
            XmlFormatter( new FileOutputStream(file) )
        {
        }
        

        // ------------------------------------------------------
        virtual ~XmlStdioPrinter()
        {
            delete outputStream();
        }
    };
}

#endif // __cxxtest__XmlStdioPrinter_h__
