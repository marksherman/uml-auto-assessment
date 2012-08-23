#ifndef __CXXTEST__LINKED_LIST_H
#define __CXXTEST__LINKED_LIST_H

#include <cxxtest/Flags.h>

namespace CxxTest 
{
    struct List;
    class Link;

    struct List
    {
        Link *_head;
        Link *_tail;

        void initialize() { _head = _tail = 0; }

        Link *head();
        const Link *head() const;
        Link *tail();
        const Link *tail() const;

        unsigned size() const;
        Link *nth( unsigned n );

        void activateAll();
        void leaveOnly( const Link &link );
    };

    class Link
    {        
    public:
        Link() : _next( 0 ), _prev( 0 ), _active( true ) {}
        virtual ~Link() {}

        bool active() const { return _active; }
        void setActive( bool value = true ) { _active = value; }

        Link *justNext() { return _next; }
        Link *justPrev() { return _prev; }
        
        Link *next() { Link *l = _next; while ( l && !l->_active ) l = l->_next; return l; }
        Link *prev() { Link *l = _prev; while ( l && !l->_active ) l = l->_prev; return l; }
        const Link *next() const { Link *l = _next; while ( l && !l->_active ) l = l->_next; return l; }
        const Link *prev() const { Link *l = _prev; while ( l && !l->_active ) l = l->_prev; return l; }

        virtual bool setUp() = 0;
        virtual bool tearDown() = 0;

        void attach( List &list )
        {
            if ( list._tail )
                list._tail->_next = this;

            _prev = list._tail;
            _next = 0;
            
            if ( list._head == 0 )
                list._head = this;
            list._tail = this;
        }

        void detach( List &list )
        {
            if ( _prev )
                _prev->_next = _next;
            else
                list._head = _next;
            
            if ( _next )
                _next->_prev = _prev;
            else
                list._tail = _prev;
        }

    private:
        Link *_next;
        Link *_prev;
        bool _active;

        Link( const Link & );
        Link &operator=( const Link & );
    };
}

#endif // __CXXTEST__LINKED_LIST_H

