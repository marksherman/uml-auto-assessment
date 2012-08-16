#ifndef __CXXTEST_STDVALUETRAITS_H
#define __CXXTEST_STDVALUETRAITS_H

//
// This file defines ValueTraits for std:: stuff.
// It is #included by <cxxtest/ValueTraits.h> if you
// define CXXTEST_HAVE_STD
//

#include <cxxtest/ValueTraits.h>
#include <cxxtest/StdString.h>

#ifdef _CXXTEST_OLD_STD
#   define CXXTEST_STD(x) x
#else // !_CXXTEST_OLD_STD
#   define CXXTEST_STD(x) std::x
#endif // _CXXTEST_OLD_STD

#ifndef CXXTEST_USER_VALUE_TRAITS

namespace CxxTest
{
    //
    // NOTE: This should have been
    // template<class Char, class Traits, class Allocator>
    // class ValueTraits< std::basic_string<Char, Traits, Allocator> > {};
    // But MSVC doesn't support it (yet).
    //

    CXXTEST_TEMPLATE_INSTANTIATION
    class ValueTraits<const CXXTEST_STD(string)>
    {
        CXXTEST_STD(string) _s;
        
    public:
        ValueTraits( const CXXTEST_STD(string) &s ) : _s()
        {
            _s.append( 1, '\"' );
            for ( unsigned i = 0; i < s.length(); ++ i ) {
                char c[sizeof("\\xXX")];
                charToString( s[i], c );
                _s.append( c );
            }
            _s.append( 1, '\"' );
        }
        const char *asString( void ) const { return _s.c_str(); }
    };

    CXXTEST_COPY_CONST_TRAITS( CXXTEST_STD(string) );

#ifndef _CXXTEST_OLD_STD
    CXXTEST_TEMPLATE_INSTANTIATION
    class ValueTraits<const CXXTEST_STD(basic_string<wchar_t>)>
    {
        CXXTEST_STD(string) _s;
    public:
        ValueTraits( const CXXTEST_STD(basic_string<wchar_t>) &s ) : _s()
        {
            _s.append( "L\"" );
            for ( unsigned i = 0; i < s.length(); ++ i ) {
                char c[sizeof("\\x12345678")];
                charToString( (unsigned long)s[i], c );
                _s.append( c );
            }
            _s.append( 1, '\"' );
        }
        const char *asString( void ) const { return _s.c_str(); }
    };

    CXXTEST_COPY_CONST_TRAITS( CXXTEST_STD(basic_string<wchar_t>) );
#endif // _CXXTEST_OLD_STD
};

#endif // CXXTEST_USER_VALUE_TRAITS

#endif // __CXXTEST_STDVALUETRAITS_H
