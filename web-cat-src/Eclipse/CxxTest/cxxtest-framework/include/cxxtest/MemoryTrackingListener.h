#ifndef __cxxtest__MemoryTrackingListener_h__
#define __cxxtest__MemoryTrackingListener_h__

//
// This file defines a listener that can be used for advanced memory tracking
// (which attempts to eliminate false memory leaks when test cases fail,
// preventing cleanup of resources allocated in them.)
// 

#include <cxxtest/TestListener.h>
#include <dereferee.h>

#include <iostream>
using namespace std;

namespace CxxTest 
{

    class MemoryTrackingListener : public TestListener
    {
    public:
        // ----------------------------------------------------------
        MemoryTrackingListener()
        {
        }

        
        // ----------------------------------------------------------
        enum
        {
            GET = 0,
            ADVANCE
        };


        // ----------------------------------------------------------
        static unsigned int tagAction(int action)
        {
            static unsigned int _tag = 0;
            
            if (action == ADVANCE)
            {
                _tag = (_tag + 1) & 0x7FFFFFFF;                
            }

            return _tag;
        }


        // ----------------------------------------------------------
        void enterTest(const TestDescription& d)
        {
            tagAction(ADVANCE);
        }
        

        // ----------------------------------------------------------
        void leaveTest(const TestDescription& d)
        {
            // If this test failed, we give the user the benefit of the
            // doubt that any memory allocated in the test would have been
            // freed properly, and flag them as such.

            if (tracker().testFailed())
            {
                sweep();
            }
        }


    private:
        // ----------------------------------------------------------
        static void sweepVisitor(
            Dereferee::allocation_info& allocInfo, void* arg)
        {
            unsigned int allocTag =
                ((unsigned int) allocInfo.user_info()) & 0x7FFFFFFF;

            // If the memory block has the current tag, mark it as "probably
            // not leaked" so we don't report on it later.

            if (allocTag == tagAction(GET))
            {
                allocTag |= 0x80000000;
                allocInfo.set_user_info((void*) allocTag);
            }
        }


        // ----------------------------------------------------------
        void sweep()
        {
            Dereferee::visit_allocations(
                &MemoryTrackingListener::sweepVisitor, this);
        }
    };
    
} // end namespace CxxTest


#endif // __cxxtest__MemoryTrackingListener_h__
