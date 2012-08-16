#ifndef __CXXTEST__GLOBAL_FIXTURE_H
#define __CXXTEST__GLOBAL_FIXTURE_H

#include <cxxtest/LinkedList.h>

namespace CxxTest 
{
    class GlobalFixture : public Link
    {
    public:
        virtual bool setUpWorld() { return true; }
        virtual bool tearDownWorld() { return true; }
        virtual bool setUp() { return true; }
        virtual bool tearDown() { return true; }
        
        GlobalFixture() { attach( _list ); }
        ~GlobalFixture() { detach( _list ); }
        
        static GlobalFixture *firstGlobalFixture() { return (GlobalFixture *)_list.head(); }
        static GlobalFixture *lastGlobalFixture() { return (GlobalFixture *)_list.tail(); }
        GlobalFixture *nextGlobalFixture() { return (GlobalFixture *)next(); }
        GlobalFixture *prevGlobalFixture() { return (GlobalFixture *)prev(); }

    private:
        static List _list;
    };
}

#endif // __CXXTEST__GLOBAL_FIXTURE_H

