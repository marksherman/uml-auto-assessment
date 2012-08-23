#ifndef __cxxtest__OutputStream_h__
#define __cxxtest__OutputStream_h__

//
// This header defines a class that is somewhat analogous to the standard
// library's ostream class, to provide a simple way to stream test listener
// output without relying on those standard library classes.
//

#include <cxxtest/SafeString.h>
#include <stdio.h>

namespace CxxTest
{
    //----------------------------------------------------------------------
    /**
     * A simple output stream class, analogous to the one defined in the
     * C++ standard library.
     */
    class OutputStream
    {
    public:
        //~ Nested Types .....................................................

        // ------------------------------------------------------
        /**
         * A manipulator is a function that takes the output stream as its
         * single argument and performs some operation on the stream.
         */
        typedef void (*Manipulator)( OutputStream& );
        

        //~ Constructors/Destructors .........................................

        // ------------------------------------------------------
        /**
         * Releases any resources currently used by the output stream. The
         * default implementation does nothing.
         */
        virtual ~OutputStream() {}
        

        //~ Methods ..........................................................

        // ------------------------------------------------------
        /**
         * Flushes any currently buffered contents of the output stream to
         * their destination. The default implementation does nothing.
         */
        virtual void flush() {}
        

        // ------------------------------------------------------
        /**
         * Outputs an unsigned integer to the stream. The default
         * implementation does nothing.
         */
        virtual OutputStream& operator<<( unsigned /*number*/ )
        {
            return *this;
        }
        

        // ------------------------------------------------------
        /**
         * Outputs a C-style string to the stream. The default implementation
         * does nothing.
         */
        virtual OutputStream& operator<<( const char* /*string*/ )
        {
            return *this;
        }


        // ------------------------------------------------------
        /**
         * Outputs a CxxTest SafeString to the stream. The default
         * implementation simply delegates to the C-style string overload of
         * this operator.
         */
        virtual OutputStream& operator<<( const SafeString& str )
        {
            return (*this << str.c_str());
        }


        // ------------------------------------------------------
        /**
         * Performs the operations of the given manipulator on the output
         * stream.
         *
         * @param manipulator the manipulator
         */
        virtual OutputStream& operator<<( Manipulator manipulator )
        {
            manipulator( *this );
            return *this;
        }
        

        // ------------------------------------------------------
        /**
         * A manipulator that outputs a newline character to the output stream
         * and then flushes its contents.
         */
        static void endl( OutputStream& o )
        {
            (o << "\n").flush();
        }


        // ------------------------------------------------------
        //
        // Class-specific overloads so that adapters do not get tracked by
        // the Dereferee memory manager, if Dereferee is also in use.
        //
        void* operator new(size_t size) { return malloc(size); }
        void operator delete(void* ptr) { free(ptr); }
    };


    //----------------------------------------------------------------------
    class FileOutputStream : public OutputStream
    {
    public:
        // ------------------------------------------------------
        FileOutputStream( FILE *o, bool own = false ) : _o(o), _own(own) {}


        // ------------------------------------------------------
        ~FileOutputStream()
        {
            if (_own)
                fclose(_o);
        }
        
        
        // ------------------------------------------------------
        void flush()
        {
            fflush( _o );
        }


        // ------------------------------------------------------
        OutputStream& operator<<( unsigned i )
        {
            fprintf( _o, "%u", i );
            return *this;
        }


        // ------------------------------------------------------
        OutputStream& operator<<( const char *s )
        {
            fputs( s, _o );
            return *this;
        }


        // ------------------------------------------------------
        OutputStream& operator<<( Manipulator m )
        {
            return OutputStream::operator<<( m );
        }


    private:
        // ------------------------------------------------------
        FileOutputStream( const FileOutputStream & );


        // ------------------------------------------------------
        FileOutputStream &operator=( const FileOutputStream & );


        //~ Instance variables ...........................................

        FILE *_o;
        bool _own;
    };
    
} // end namespace CxxTest

#endif // __cxxtest__OutputStream_h__
