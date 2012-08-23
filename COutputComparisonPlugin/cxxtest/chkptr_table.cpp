/*
 *	This file is part of Web-CAT Eclipse Plugins.
 *
 *	Web-CAT is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Web-CAT is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Web-CAT; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * ACKNOWLEDGMENTS:
 * This code is based on and extends the work done by Scott M. Pike and
 * Bruce W. Weide at The Ohio State University and Joseph E. Hollingsworth of
 * Indiana University SE in "Checkmate: Cornering C++ Dynamic Memory Errors
 * With Checked Pointers", Proc. of the 31st SIGCSE Technical Symposium on
 * CSE, ACM Press, March 2000.
 */
   
#include "chkptr.h"
#include <stdarg.h>

#if defined(CXXTEST_TRACE_STACK) && !defined(CHKPTR_STACK_WINDOW_SIZE)
#  define CHKPTR_STACK_WINDOW_SIZE 8
#endif

#ifdef CHKPTR_BASIC_HEAP_CHECK
#	define SAFETY_SIZE 16
#else
#	define SAFETY_SIZE 0
#endif

#define SAFETY_CHAR '!'

#ifdef CXXTEST_TRACE_STACK
#ifndef CHKPTR_STACK_TRACE_INITIAL_PREFIX
#   define CHKPTR_STACK_TRACE_INITIAL_PREFIX CHKPTR_PREFIX "  allocated using: "
#endif
#ifndef CHKPTR_STACK_TRACE_OTHER_PREFIX
#   define CHKPTR_STACK_TRACE_OTHER_PREFIX CHKPTR_PREFIX   "      called from: "
#endif
#endif

namespace ChkPtr
{

static const char* __error_messages[] = {
	"Called \"delete\" on array pointer (should use \"delete[]\")",
	"Called \"delete[]\" on non-array pointer (should use \"delete\")",
	"Freed uninitialized pointer",
	"Freed memory that was already freed",
	"Dereferenced uninitialized pointer",
	"Dereferenced null pointer",
	"Dereferenced freed memory",
	"Checked pointers cannot be used with memory not allocated with \"new\" or \"new[]\"",
	"Memory leak caused by last valid pointer to memory block going out of scope",
	"Memory leak caused by last valid pointer to memory block being overwritten",
	"Comparison with a dead pointer may result in unpredictable behavior",
	"Indexed a non-array pointer",
	"Invalid array index (%d); valid indices are [0..%lu]",
	"Freed memory that was not dynamically allocated or was already freed",
	"Memory %s block was corrupted; likely invalid array indexing or pointer arithmetic",
};

__checked_pointer_table __manager __attribute__((init_priority(101)));

void __stderr_error_handler(bool fatal, const char* msg);

#define HASH(address) \
	(reinterpret_cast<unsigned long>(address) % CHECKED_HASHTABLE_SIZE)
	
#define HASH_UNCHECKED(address) \
	(reinterpret_cast<unsigned long>(address) % UNCHECKED_HASHTABLE_SIZE)

#define HASH_PROXY(address) \
	(reinterpret_cast<unsigned long>(address) % PROXY_HASHTABLE_SIZE)

// ------------------------------------------------------------------
void __checked_pointer_table::__stderr_reporter::beginReport(
	int* tagList)
{
	int numLeaks = 0;

	while(*tagList != CHKPTR_REPORT_END)
	{
		int tag = *tagList++;
		int value = *tagList++;
		
		switch(tag)
		{
			case CHKPTR_REPORT_NUM_LEAKS:
				numLeaks = value;
				break;
			
			case CHKPTR_REPORT_TOTAL_BYTES_ALLOCATED:
				totalBytesAllocated = value;
				break;
				
			case CHKPTR_REPORT_MAX_BYTES_IN_USE:
				maxBytesInUse = value;
				break;
				
			case CHKPTR_REPORT_NUM_CALLS_NEW:
				numCallsToNew = value;
				break;

			case CHKPTR_REPORT_NUM_CALLS_ARRAY_NEW:
				numCallsToArrayNew = value;
				break;

			case CHKPTR_REPORT_NUM_CALLS_DELETE:
				numCallsToDelete = value;
				break;

			case CHKPTR_REPORT_NUM_CALLS_ARRAY_DELETE:
				numCallsToArrayDelete = value;
				break;

			case CHKPTR_REPORT_NUM_CALLS_DELETE_NULL:
				numCallsToDeleteNull = value;
				break;
		}
	}

	if(numLeaks > 0)
	{
		printf(CHKPTR_PREFIX "%d memory leaks were detected:\n", numLeaks);
		printf(CHKPTR_PREFIX "--------\n");
	}
	else
	{
		printf(CHKPTR_PREFIX "No memory leaks detected.\n");
	}
}

// ------------------------------------------------------------------
#ifdef CXXTEST_TRACE_STACK
void __checked_pointer_table::__stderr_reporter::report(const void* address,
	size_t size, const char* /*filename*/, int /* line */ )
{
	printf(CHKPTR_PREFIX "Leaked %lu bytes at address %p\n",
		(unsigned long)size, address);

	printf(getStackTrace(true, CHKPTR_STACK_WINDOW_SIZE,
			(CxxTest::StackElem*)(((char*)address) + size),
			CHKPTR_STACK_TRACE_INITIAL_PREFIX,
			CHKPTR_STACK_TRACE_OTHER_PREFIX).c_str());
	printf(CHKPTR_PREFIX "\n");
}
#else
void __checked_pointer_table::__stderr_reporter::report(const void* address,
	size_t size, const char* filename, int line)
{
	if(line == 0)
	{
		printf(CHKPTR_PREFIX "Leaked %lu bytes at address %p\n",
			(unsigned long)size, address);
	}
	else
	{
		printf(CHKPTR_PREFIX "Leaked %lu bytes at address %p, allocated at %s:%d\n",
			(unsigned long)size, address, filename, line);
	}
	printf(CHKPTR_PREFIX "\n");
}
#endif

// ------------------------------------------------------------------
void __checked_pointer_table::__stderr_reporter::reportsTruncated(
	int reportsLogged, int actualCount)
{
	printf(CHKPTR_PREFIX "\n");
	printf(CHKPTR_PREFIX "(only %d of %d leaks shown)\n", reportsLogged, actualCount);
}

// ------------------------------------------------------------------
void __checked_pointer_table::__stderr_reporter::endReport()
{
	printf(CHKPTR_PREFIX "\n");
	printf(CHKPTR_PREFIX "Memory usage statistics:\n");
	printf(CHKPTR_PREFIX "--------\n");
	printf(CHKPTR_PREFIX "Total memory allocated during execution:   %d bytes\n", totalBytesAllocated);
	printf(CHKPTR_PREFIX "Maximum memory in use during execution:    %d bytes\n", maxBytesInUse);
	printf(CHKPTR_PREFIX "Number of calls to new:                    %d\n", numCallsToNew);
	printf(CHKPTR_PREFIX "Number of calls to delete (non-null):      %d\n", numCallsToDelete);
	printf(CHKPTR_PREFIX "Number of calls to new[]:                  %d\n", numCallsToArrayNew);
	printf(CHKPTR_PREFIX "Number of calls to delete[] (non-null):    %d\n", numCallsToArrayDelete);
	printf(CHKPTR_PREFIX "Number of calls to delete/delete[] (null): %d\n", numCallsToDeleteNull);
}

// ------------------------------------------------------------------
__checked_pointer_table::__checked_pointer_table()
{
	uninitHandle = malloc(4);

	reportAtEnd = true;
	numReportsToLog = 20;

	nextTag = 0;
	numEntries = 0;
	numUnchecked = 0;

	totalBytesAllocated = 0;
	maxBytesInUse = 0;
	numCallsToNew = 0;
	numCallsToArrayNew = 0;
	numCallsToDelete = 0;
	numCallsToArrayDelete = 0;
	numCallsToDeleteNull = 0;
	internalCall = false;

	for(int i = 0; i < CHECKED_HASHTABLE_SIZE; i++)
		table[i] = 0;

	for(int i = 0; i < UNCHECKED_HASHTABLE_SIZE; i++)
		uncheckedTable[i] = 0;
		
	setErrorHandler(&__stderr_error_handler);
	setReporter(&__stderr_reporter_obj, false);
}

// ------------------------------------------------------------------
__checked_pointer_table::~__checked_pointer_table()
{
	if(reportAtEnd)
		reportAllocations();
		
	if(ownReporter)
		delete reporter;
		
	free(uninitHandle);
}

// ------------------------------------------------------------------
unsigned long __checked_pointer_table::getTag()
{
	return nextTag++;
}

// ------------------------------------------------------------------
unsigned long __checked_pointer_table::moveToChecked(const void* address)
{
	int index = HASH_UNCHECKED(address);
	__node* node = uncheckedTable[index];

	if(node->address == address)
	{
		uncheckedTable[index] = node->next;
	}
	else
	{
		__node* nextNode = node->next;
		while(nextNode->address != address)
		{
			node = nextNode;
			nextNode = nextNode->next;
		}
		
		node->next = nextNode->next;
		node = nextNode;
	}
	numUnchecked--;

	index = HASH(address);
	
	node->next = table[index];
	table[index] = node;
	numEntries++;
	
	return node->tag;	
}

// ------------------------------------------------------------------
void __checked_pointer_table::addUnchecked(const void* address, bool isArray,
	size_t size, unsigned long tag, const char* filename, int line)
{
	int index = HASH_UNCHECKED(address);
	__node* node = (__node*)malloc(sizeof(__node));
	
	node->address = address;
	node->isArray = isArray;
	node->size = size;
	node->tag = tag;
	node->filename = filename;
	node->line = line;
	node->refCount = 0;

	node->next = uncheckedTable[index];
	uncheckedTable[index] = node;
	numUnchecked++;
}

// ------------------------------------------------------------------
void __checked_pointer_table::remove(const void* address)
{
	int index = HASH(address);
	__node* node = table[index];
	
	if(node->address == address)
	{
		table[index] = node->next;
		free(node); 
	}
	else
	{
		__node* nextNode = node->next;
		while(nextNode->address != address)
		{
			node = nextNode;
			nextNode = nextNode->next;
		}
		
		node->next = nextNode->next;
		free(nextNode);
	}
	
	numEntries--;
}

// ------------------------------------------------------------------
void __checked_pointer_table::removeUnchecked(const void* address)
{
	int index = HASH_UNCHECKED(address);
	__node* node = uncheckedTable[index];
	
	if(node->address == address)
	{
		uncheckedTable[index] = node->next;
		free(node);
	}
	else
	{
		__node* nextNode = node->next;
		while(nextNode->address != address)
		{
			node = nextNode;
			nextNode = nextNode->next;
		}
		
		node->next = nextNode->next;
		free(nextNode);
	}
	
	numUnchecked--;
}

// ------------------------------------------------------------------
bool __checked_pointer_table::contains(const void* address, unsigned long tag)
{
	int index = HASH(address);
	__node* node = table[index];
	
	while((node != 0) && (node->address != address))
		node = node->next;
	
	if(node == 0)
		return false;
	else
		return (node->tag == tag);
}

// ------------------------------------------------------------------
find_address_results __checked_pointer_table::findAddress(const void* address,
	unsigned long& tag)
{
	int index = HASH_UNCHECKED(address);
	__node* node = uncheckedTable[index];

	while((node != 0) && (node->address != address))
		node = node->next;
	
	if(node != 0)
		return address_found_unchecked;
		
	index = HASH(address);
	node = table[index];
	
	while((node != 0) && (node->address != address))
		node = node->next;
	
	if(node != 0)
	{
		tag = node->tag;
		return address_found_checked;
	}

	return address_not_found;
	
}

// ------------------------------------------------------------------
void* __checked_pointer_table::getUninitHandle()
{
	return uninitHandle;
}

// ------------------------------------------------------------------
void __checked_pointer_table::retain(const void* address)
{
	int index = HASH(address);
	__node* node = table[index];
	
	while(node->address != address)
		node = node->next;
		
	node->refCount++;
}

// ------------------------------------------------------------------
void __checked_pointer_table::release(const void* address)
{
	int index = HASH(address);
	__node* node = table[index];
	
	while(node->address != address)
		node = node->next;
		
	node->refCount--;
}

// ------------------------------------------------------------------
unsigned long __checked_pointer_table::getRefCount(const void* address)
{
	int index = HASH(address);
	__node* node = table[index];
	
	while(node->address != address)
		node = node->next;
		
	return node->refCount;
}

// ------------------------------------------------------------------
size_t __checked_pointer_table::getSize(const void* address)
{
	int index = HASH_UNCHECKED(address);
	__node* node = uncheckedTable[index];

	while((node != 0) && (node->address != address))
		node = node->next;

	if(node != 0)
		return node->size;

	index = HASH(address);
	node = table[index];

	while((node != 0) && (node->address != address))
		node = node->next;

	if(node != 0)		
		return node->size;

	return (size_t)-1;
}

// ------------------------------------------------------------------
bool __checked_pointer_table::isArray(const void* address)
{
	int index = HASH_UNCHECKED(address);
	__node* node = uncheckedTable[index];

	while((node != 0) && (node->address != address))
		node = node->next;

	if(node != 0)
		return node->isArray;

	index = HASH(address);
	node = table[index];

	while(node->address != address)
		node = node->next;

	return node->isArray;
}

// ------------------------------------------------------------------
void __checked_pointer_table::logError(bool fatal, int code, ...)
{
	char* msg = 0;

	va_list args;
	va_start(args, code);
	vasprintf(&msg, __error_messages[code], args);	
	va_end(args);

	(*errorHandler)(fatal, msg);
	free(msg);
}

// ------------------------------------------------------------------
void __checked_pointer_table::setErrorHandler(
	void (*handler)(bool, const char*))
{
	errorHandler = handler;
}

// ------------------------------------------------------------------
void __checked_pointer_table::reportAllocations()
{
	int numLeaks = numEntries + numUnchecked;

	int tags[] = {
		CHKPTR_REPORT_NUM_LEAKS,				numLeaks,
		CHKPTR_REPORT_TOTAL_BYTES_ALLOCATED,	totalBytesAllocated,
		CHKPTR_REPORT_MAX_BYTES_IN_USE,			maxBytesInUse,
		CHKPTR_REPORT_NUM_CALLS_NEW,			numCallsToNew,
		CHKPTR_REPORT_NUM_CALLS_ARRAY_NEW,		numCallsToArrayNew,
		CHKPTR_REPORT_NUM_CALLS_DELETE,			numCallsToDelete,
		CHKPTR_REPORT_NUM_CALLS_ARRAY_DELETE,	numCallsToArrayDelete,
		CHKPTR_REPORT_NUM_CALLS_DELETE_NULL,	numCallsToDeleteNull,
		CHKPTR_REPORT_END
	};

	reporter->beginReport(tags);

	int reportsLogged = 0;

	if(numEntries > 0)
	{
		for(int i = 0; i < CHECKED_HASHTABLE_SIZE; i++)
		{
			for(__node* p = table[i]; p != 0; p = p->next)
			{
				if(reportsLogged < numReportsToLog)
				{
					reporter->report(p->address, p->size, p->filename, p->line);
					reportsLogged++;
				}
			}
		}
	}

	if(numUnchecked > 0)
	{
		for(int i = 0; i < UNCHECKED_HASHTABLE_SIZE; i++)
		{
			for(__node* p = uncheckedTable[i]; p != 0; p = p->next)
			{
				if(reportsLogged < numReportsToLog)
				{
					reporter->report(p->address, p->size, p->filename, p->line);
					reportsLogged++;
				}
			}
		}
	}
	
	if(numLeaks > reportsLogged)
	{
		reporter->reportsTruncated(reportsLogged, numLeaks);
	}	
	
	reporter->endReport();
}

// ------------------------------------------------------------------
void __checked_pointer_table::setReporter(chkptr_reporter* r, bool own)
{
	reporter = r;
	ownReporter = own;
}

// ------------------------------------------------------------------
void __checked_pointer_table::setReportAtEnd(bool value)
{
	reportAtEnd = value;
}

// ------------------------------------------------------------------
void __checked_pointer_table::getStatistics(int& totalBytes, int& maxBytes,
	int& numNew, int& numArrayNew, int& numDelete, int& numArrayDelete) const
{
	totalBytes = totalBytesAllocated;
	maxBytes = maxBytesInUse;
	numNew = numCallsToNew;
	numArrayNew = numCallsToArrayNew;
	numDelete = numCallsToDelete;
	numArrayDelete = numCallsToArrayDelete;
}

// ------------------------------------------------------------------
void __stderr_error_handler(bool fatal, const char* msg)
{
	fprintf(stdout, CHKPTR_PREFIX "Pointer %s: %s\n",
		fatal? "error" : "warning", msg);
}

} // namespace ChkPtr


// ------------------------------------------------------------------
void* operator new(size_t size)
{
	ChkPtr::__manager.numCallsToNew++;
	
	ChkPtr::__manager.currentBytesAllocated += size;
	ChkPtr::__manager.totalBytesAllocated += size;
	if(ChkPtr::__manager.currentBytesAllocated > ChkPtr::__manager.maxBytesInUse)
		ChkPtr::__manager.maxBytesInUse = ChkPtr::__manager.currentBytesAllocated;

	int allocSize = size
#ifdef CXXTEST_TRACE_STACK
		+ CxxTest::stackTraceSize(CHKPTR_STACK_WINDOW_SIZE)
#endif
		+ (2 * SAFETY_SIZE)
	;

	void* allocPtr = malloc(allocSize);
	char* ptr = ((char*)allocPtr) + SAFETY_SIZE;

#ifdef CHKPTR_BASIC_HEAP_CHECK
	memset(allocPtr, SAFETY_CHAR, SAFETY_SIZE);
	memset(ptr + size, SAFETY_CHAR, SAFETY_SIZE);
#endif
	
#ifdef CXXTEST_TRACE_STACK
    CxxTest::saveStackTraceWindow(
		(CxxTest::StackElem*)((char*)ptr + size + SAFETY_SIZE),
		CHKPTR_STACK_WINDOW_SIZE);
#endif

	unsigned long tag = ChkPtr::__manager.getTag();
	ChkPtr::__manager.addUnchecked(ptr, false, size, tag, "", 0);

	return ptr;
}

// ------------------------------------------------------------------
void* operator new(size_t size, const std::nothrow_t& /*nothrow*/)
{
	return ::operator new(size);
}

// ------------------------------------------------------------------
void* operator new[](size_t size)
{
	ChkPtr::__manager.numCallsToArrayNew++;

	ChkPtr::__manager.currentBytesAllocated += size;
	ChkPtr::__manager.totalBytesAllocated += size;
	if(ChkPtr::__manager.currentBytesAllocated > ChkPtr::__manager.maxBytesInUse)
		ChkPtr::__manager.maxBytesInUse = ChkPtr::__manager.currentBytesAllocated;

	int allocSize = size
#ifdef CXXTEST_TRACE_STACK
		+ CxxTest::stackTraceSize(CHKPTR_STACK_WINDOW_SIZE)
#endif
		+ (2 * SAFETY_SIZE)
	;

	void* allocPtr = malloc(allocSize);
	char* ptr = ((char*)allocPtr) + SAFETY_SIZE;

#ifdef CHKPTR_BASIC_HEAP_CHECK
	memset(allocPtr, SAFETY_CHAR, SAFETY_SIZE);
	memset(ptr + size, SAFETY_CHAR, SAFETY_SIZE);
#endif

#ifdef CXXTEST_TRACE_STACK
    CxxTest::saveStackTraceWindow(
		(CxxTest::StackElem*)((char*)ptr + size + SAFETY_SIZE),
		CHKPTR_STACK_WINDOW_SIZE);
#endif

	unsigned long tag = ChkPtr::__manager.getTag();
	ChkPtr::__manager.addUnchecked(ptr, true, size, tag, "", 0);

	return ptr;
}

// ------------------------------------------------------------------
void* operator new[](size_t size, const std::nothrow_t& /*nothrow*/)
{
	return ::operator new[](size);
}

// ------------------------------------------------------------------
void operator delete(void* address)
{
	if(address == ChkPtr::__manager.getUninitHandle())
	{
		ChkPtr::__manager.internalCall = true;
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DELETE_UNINITIALIZED);
		ChkPtr::__manager.internalCall = false;
		return;
	}
	else if(address != 0)
	{
		ChkPtr::__manager.numCallsToDelete++;

		size_t size = ChkPtr::__manager.getSize(address);
#ifdef CHKPTR_BASIC_HEAP_CHECK
		if(size == (size_t)-1)
		{
			ChkPtr::__manager.internalCall = true;
			ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DELETE_NOT_DYNAMIC);
			ChkPtr::__manager.internalCall = false;
			return;
		}
		
		if(ChkPtr::__manager.isArray(address))
		{
			ChkPtr::__manager.internalCall = true;
			ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DELETE_ARRAY);
			ChkPtr::__manager.internalCall = false;
		}		
#endif
		{
			ChkPtr::__manager.currentBytesAllocated -= size;

			unsigned long dummy;
			ChkPtr::find_address_results found = ChkPtr::__manager.findAddress(address, dummy);
			
			if(found == ChkPtr::address_found_checked)
				ChkPtr::__manager.remove(address);
			else if(found == ChkPtr::address_found_unchecked)
				ChkPtr::__manager.removeUnchecked(address);

			void* realAddr = (char*)address - SAFETY_SIZE;

#ifdef CHKPTR_BASIC_HEAP_CHECK
			char lowSafetyStr[SAFETY_SIZE], highSafetyStr[SAFETY_SIZE];
			bool lowDamage = false, highDamage = false;

			memset(lowSafetyStr, SAFETY_CHAR, SAFETY_SIZE);
			memset(highSafetyStr, SAFETY_CHAR, SAFETY_SIZE);

			if(memcmp((char*)realAddr, lowSafetyStr, SAFETY_SIZE))
				lowDamage = true;

			if(memcmp(((char*)address) + size, highSafetyStr, SAFETY_SIZE))
				highDamage = true;

			if(lowDamage || highDamage)
			{
				const char* damageStr;
				if(lowDamage && highDamage)
					damageStr = "before and after";
				else if(lowDamage)
					damageStr = "before";
				else
					damageStr = "after";

				ChkPtr::__manager.internalCall = true;
				ChkPtr::__manager.logError(false, ChkPtr::PTRERR_MEMORY_CORRUPTION, damageStr);
				ChkPtr::__manager.internalCall = false;
			}
#endif
			// Zero out the memory before freeing it. This is useful if a
			// dangling pointer to an object with a vtable has a method
			// called on it; in this case, a null pointer dereference
			// will result.
			bzero(realAddr, size + 2 * SAFETY_SIZE);

			free(realAddr);
		}
	}
	else
	{
		ChkPtr::__manager.numCallsToDeleteNull++;
	}
}

// ------------------------------------------------------------------
void operator delete[](void* address)
{
	if(address == ChkPtr::__manager.getUninitHandle())
	{
		ChkPtr::__manager.internalCall = true;
		ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DELETE_UNINITIALIZED);
		ChkPtr::__manager.internalCall = false;
		return;
	}
	else if(address != 0)
	{
		ChkPtr::__manager.numCallsToArrayDelete++;

		size_t size = ChkPtr::__manager.getSize(address);
#ifdef CHKPTR_BASIC_HEAP_CHECK
		if(size == (size_t)-1)
		{
			ChkPtr::__manager.internalCall = true;
			ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DELETE_NOT_DYNAMIC);
			ChkPtr::__manager.internalCall = false;
			return;
		}

		if(!ChkPtr::__manager.isArray(address))
		{
			ChkPtr::__manager.internalCall = true;
			ChkPtr::__manager.logError(true, ChkPtr::PTRERR_DELETE_NONARRAY);
			ChkPtr::__manager.internalCall = false;
		}		
#endif
		{
			ChkPtr::__manager.currentBytesAllocated -= size;

			unsigned long dummy;
			ChkPtr::find_address_results found = ChkPtr::__manager.findAddress(address, dummy);
			
			if(found == ChkPtr::address_found_checked)
				ChkPtr::__manager.remove(address);
			else if(found == ChkPtr::address_found_unchecked)
				ChkPtr::__manager.removeUnchecked(address);
		
			void* realAddr = (char*)address - SAFETY_SIZE;
			
#ifdef CHKPTR_BASIC_HEAP_CHECK
			char lowSafetyStr[SAFETY_SIZE], highSafetyStr[SAFETY_SIZE];
			bool lowDamage = false, highDamage = false;

			memset(lowSafetyStr, SAFETY_CHAR, SAFETY_SIZE);
			memset(highSafetyStr, SAFETY_CHAR, SAFETY_SIZE);

			if(memcmp((char*)realAddr, lowSafetyStr, SAFETY_SIZE))
				lowDamage = true;

			if(memcmp(((char*)address) + size, highSafetyStr, SAFETY_SIZE))
				highDamage = true;

			if(lowDamage || highDamage)
			{
				const char* damageStr;
				if(lowDamage && highDamage)
					damageStr = "before and after";
				else if(lowDamage)
					damageStr = "before";
				else
					damageStr = "after";

				ChkPtr::__manager.internalCall = true;
				ChkPtr::__manager.logError(false, ChkPtr::PTRERR_MEMORY_CORRUPTION, damageStr);
				ChkPtr::__manager.internalCall = false;
			}
#endif
			// Zero out the memory before freeing it. This is useful if a
			// dangling pointer to an object with a vtable has a method
			// called on it; in this case, a null pointer dereference
			// will result.
			bzero(realAddr, size + 2 * SAFETY_SIZE);

			free(realAddr);
		}
	}
	else
	{
		ChkPtr::__manager.numCallsToDeleteNull++;
	}
}
