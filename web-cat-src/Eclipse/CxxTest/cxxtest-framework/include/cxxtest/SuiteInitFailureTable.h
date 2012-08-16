#ifndef __cxxtest__SuiteInitFailureTable_h__
#define __cxxtest__SuiteInitFailureTable_h__

//
// A simple table that keeps track of test suite initialization failures.
//

#include <cstdlib>
#include <cxxtest/SafeArray.h>

namespace CxxTest
{

class SuiteInitFailureTable
{    
private:
    //~ Nested Structures ....................................................

    struct Entry
    {
        char* name;
        char* reason;
    };

public:
    //~ Constructors/Destructor ..............................................

    // ----------------------------------------------------------
    ~SuiteInitFailureTable()
    {
        for (size_t i = 0; i < entries.size(); i++)
        {
            free(entries[i].name);
            free(entries[i].reason);
        }
    }


    //~ Public methods .......................................................
    
    // ----------------------------------------------------------
    void addSuite(const char* name, const char* reason)
    {
        Entry entry;
        entry.name = copyString(name);
        entry.reason = copyString(reason);
        
        entries.push_back(entry);
    }
    

    // ----------------------------------------------------------
    const char* didSuiteFail(const char* name)
    {
        for (size_t i = 0; i < entries.size(); i++)
        {
            if (strcmp(name, entries[i].name) == 0)
            {
                return entries[i].reason;
            }
        }
        
        return NULL;
    }

private:
    //~ Private methods ......................................................
    
    // ----------------------------------------------------------
    char* copyString(const char* str)
    {
    	size_t len = strlen(str);
        char* copy = (char*) malloc(len);
        strcpy(copy, str);
        return copy;
    }


    //~ Instance variables ...................................................
    
    SafeArray<Entry> entries;
};

extern SuiteInitFailureTable __cxxtest_failed_init_suites;

} // end namespace CxxTest

#endif // __cxxtest__SuiteInitFailureTable_h__
