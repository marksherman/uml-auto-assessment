#ifndef __cxxtest__ListenerList_h__
#define __cxxtest__ListenerList_h__

//
// A ListenerList is a listener that holds a linked list of other listeners
// so that multiple arbitrary listeners can be notified of test events (more
// easily than chaining multiple TeeListeners together).
//
// ListenerList does not take ownership of the listeners that are added to it.
// It is still the responsibility of the calling code to free them, if they
// are allocated on the heap.
//

#include <cxxtest/TestListener.h>

//
// Macros to simplify common iteration code in all listener methods.
//
#define _LL_LOOP(action) \
    for (Node* curr = head->next; curr != tail; curr = curr->next) { \
        TestListener* listener = curr->listener; \
        action; \
    } \


namespace CxxTest
{
    class ListenerList : public TestListener
    {
    private:
        struct Node
        {
            struct Node* prev;
            struct Node* next;
            TestListener* listener;
        };

    public:
        ListenerList()
        {
            head = (Node*) malloc(sizeof(Node));
            tail = (Node*) malloc(sizeof(Node));
            
            head->next = tail;
            head->prev = NULL;
            head->listener = NULL;
            
            tail->next = NULL;
            tail->prev = head;
            tail->listener = NULL;
        }

        virtual ~ListenerList()
        {
            Node* curr = head;
            while (curr)
            {
                Node* next = curr->next;
                free(curr);
                curr = next;
            }
        }

        void addListener(TestListener& listener)
        {
            Node* node = (Node*) malloc(sizeof(Node));
            node->listener = &listener;

            node->prev = tail->prev;
            tail->prev->next = node;
            
            node->next = tail;
            tail->prev = node;
        }

        void enterWorld( const WorldDescription &d )
        {
            _LL_LOOP( listener->enterWorld(d) );
        }

        void enterSuite( const SuiteDescription &d )
        {
            _LL_LOOP( listener->enterSuite(d) );
        }
        
        void enterTest( const TestDescription &d )
        {
            _LL_LOOP( listener->enterTest(d) );
        }
        
        void trace( const char *file, unsigned line, const char *expression )
        {
            _LL_LOOP( listener->trace(file, line, expression) );
        }
        
        void warning( const char *file, unsigned line, const char *expression )
        {
            _LL_LOOP( listener->warning(file, line, expression) );
        }
        
        void failedTest( const char *file, unsigned line, const char *expression )
        {
            _LL_LOOP( listener->failedTest(file, line, expression) );
        }
        
        void failedAssert( const char *file, unsigned line, const char *expression )
        {
            _LL_LOOP( listener->failedAssert(file, line, expression) );
        }
        
        void failedAssertEquals( const char *file, unsigned line,
                                 const char *xStr, const char *yStr,
                                 const char *x, const char *y )
        {
            _LL_LOOP( listener->failedAssertEquals(file, line, xStr, yStr, x, y) );
        }

        void failedAssertSameData( const char *file, unsigned line,
                                   const char *xStr, const char *yStr,
                                   const char *sizeStr, const void *x,
                                   const void *y, unsigned size )
        {
            _LL_LOOP( listener->failedAssertSameData(file, line, xStr, yStr, sizeStr, x, y, size) );
        }
        
        void failedAssertDelta( const char *file, unsigned line,
                                const char *xStr, const char *yStr, const char *dStr,
                                const char *x, const char *y, const char *d )
        {
            _LL_LOOP( listener->failedAssertDelta(file, line, xStr, yStr, dStr, x, y, d) );
        }
        
        void failedAssertDiffers( const char *file, unsigned line,
                                  const char *xStr, const char *yStr,
                                  const char *value )
        {
            _LL_LOOP( listener->failedAssertDiffers(file, line, xStr, yStr, value) );
        }
        
        void failedAssertLessThan( const char *file, unsigned line,
                                   const char *xStr, const char *yStr,
                                   const char *x, const char *y )
        {
            _LL_LOOP( listener->failedAssertLessThan(file, line, xStr, yStr, x, y) );
        }
        
        void failedAssertLessThanEquals( const char *file, unsigned line,
                                         const char *xStr, const char *yStr,
                                         const char *x, const char *y )
        {
            _LL_LOOP( listener->failedAssertLessThanEquals(file, line, xStr, yStr, x, y) );
        }
        
        void failedAssertPredicate( const char *file, unsigned line,
                                    const char *predicate, const char *xStr, const char *x )
        {
            _LL_LOOP( listener->failedAssertPredicate(file, line, predicate, xStr, x) );
        }
        
        void failedAssertRelation( const char *file, unsigned line,
                                   const char *relation, const char *xStr, const char *yStr,
                                   const char *x, const char *y )
        {
            _LL_LOOP( listener->failedAssertRelation(file, line, relation, xStr, yStr, x, y) );
        }
        
        void failedAssertThrows( const char *file, unsigned line,
                                 const char *expression, const char *type,
                                 bool otherThrown )
        {
            _LL_LOOP( listener->failedAssertThrows(file, line, expression, type, otherThrown) );
        }
        
        void failedAssertThrowsNot( const char *file, unsigned line,
                                    const char *expression )
        {
            _LL_LOOP( listener->failedAssertThrowsNot(file, line, expression) );
        }
        
        void leaveTest( const TestDescription &d )
        {
            _LL_LOOP( listener->leaveTest(d) );
        }
        
        void leaveSuite( const SuiteDescription &d )
        {
            _LL_LOOP( listener->leaveSuite(d) );
        }
        
        void leaveWorld( const WorldDescription &d )
        {
            _LL_LOOP( listener->leaveWorld(d) );
        }

        void suiteInitError( const char *file, unsigned line,
                             const char *expression )
        {
            _LL_LOOP( listener->suiteInitError(file, line, expression) );
        }

    private:
        Node* head;
        Node* tail;
    };
};

#undef _LL_LOOP

#endif // __cxxtest__ListenerList_h__
