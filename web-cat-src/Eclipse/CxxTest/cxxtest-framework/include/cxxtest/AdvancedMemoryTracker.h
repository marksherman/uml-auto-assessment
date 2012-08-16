#ifndef __CXXTEST_ADVANCEDMEMORYTRACKER_H
#define __CXXTEST_ADVANCEDMEMORYTRACKER_H

//
// This file includes the declaration of functions that are used for advanced
// memory tracking (which attempts to eliminate false memory leaks when test
// cases fail, preventing cleanup of resources allocated in them.)
// 

namespace CxxTest 
{

    extern unsigned int __memory_tracking_tag;

    extern void advance_memory_tracking_tag();
    extern void memory_tracking_sweep();

} // end namespace CxxTest


#endif // __CXXTEST_ADVANCEDMEMORYTRACKER_H