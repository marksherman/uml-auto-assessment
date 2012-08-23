#ifndef __CXXTEST__XMLSTDIOPRINTER_H
#define __CXXTEST__XMLSTDIOPRINTER_H

//
// The XmlStdioPrinter is an XmlFormatter which defaults to stdout.
//

#include <cxxtest/XmlFormatter.h>
#include <stdio.h>

namespace CxxTest 
{
    class XmlStdioPrinter : public XmlFormatter
    {
    public:
        XmlStdioPrinter(FILE* file) : XmlFormatter( new Adapter(file) ) {}
        virtual ~XmlStdioPrinter() { delete outputStream(); }

    private:
        class Adapter : public OutputStream
        {
            Adapter( const Adapter & );
            Adapter &operator=( const Adapter & );
            
            FILE *_o;
            
        public:
            Adapter( FILE *o ) : _o(o) {}
            void flush() { fflush( _o ); }
            OutputStream &operator<<( unsigned i ) { fprintf( _o, "%u", i ); return *this; }
            OutputStream &operator<<( const char *s ) { fputs( s, _o ); return *this; }
            OutputStream &operator<<( Manipulator m ) { return OutputStream::operator<<( m ); }
        };
    };
}

#endif // __CXXTEST__XMLSTDIOPRINTER_H
