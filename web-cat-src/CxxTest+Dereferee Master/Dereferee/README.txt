DEREFEREE: Checked Pointer Usage in C++
=======================================
Version: 1.1.2 (10/10/2009)

Authors: Tony Allevato and Stephen Edwards
         Department of Computer Science, Virginia Tech

Project Website: <http://web-cat.org>


INTRODUCTION
============

Just like a referee monitors a sporting event and reports on rule violations
by the players, the Dereferee kit monitors and reports on error-prone pointer
usage in C++ programs. The primary target audience for this kit are
introductory C++ students learning pointers and dynamic memory management, but
the transparency afforded by this library also makes it highly usable in
existing code bases with very little modification.


REQUIREMENTS
============

Dereferee requires a C++ compiler with a very high level of standards-
compliance, due to significant use of features like partial template
specializations. Dereferee is currently known to work under the following
compilers:

 * gcc 3.4 (Cygwin)
 * gcc 4.x (Cygwin, Linux, Mac OS X)
 * Microsoft Visual C++ 2005

Furthermore, to support determining the source file location of errors that
occur at runtime, we require that it be possible to collect a backtrace at
runtime and examine the symbol table of an executable. Pre-written modules have
been written to support the following platforms:

 * gcc_macosx_platform: gcc under Mac OS X
 * gcc_bfd_platform: gcc under any platform that supports the BFD library and
      the /proc filesystem (Cygwin and various Linuxes)
 * msvc_win32_platform: Microsoft Visual C++ 2005 under Windows

When building the Dereferee library, choose the appropriate platform for your
platform. (See "BUILDING DEREFEREE" below.)


FEATURES
========
Dereferee detects and logs the following kinds of errors or other poor
programming practices:

 * Calling the incorrect version of "delete" (i.e., "delete" on an array
   allocated by "new[]" or "delete[]" on memory allocated by "new")
 * Deleting or dereferencing an uninitialized pointer
 * Deleting or dereferencing memory that has been already freed
 * Dereferencing a null pointer
 * Memory leaks caused by the last valid pointer to a live memory block going
   out of scope or being overwritten
 * Performing a comparison with a pointer that has been deleted or is
   uninitialized
 * Using the array index operator on a pointer that was not allocated using
   "new[]"
 * Referencing an array index that is outside the bounds used when allocating
   the array with "new[]"
 * Invalid use of pointer arithmetic

Dereferee can also optionally pad the ends of allocated memory blocks with
safety bytes that are checked upon deletion to determine if bad pointer
usage has caused any buffer underflows/overflows.

By default, the Dereferee memory manager will output at the end of execution
a block of summary text that includes the size and location of any leaked
memory blocks, as well as statistics about the number of calls made to
new/delete and the amounts of memory used during execution.


CONTENTS
========
This kit is structured in the following manner:

 * demo/ -- A small program used to demonstrate Dereferee
 * include/ -- Header files required for client code to use Dereferee
 * listeners/ -- A set of pre-written listener modules that interface with
       Dereferee
 * platforms/ -- A set of pre-written platform modules that interface with
       Dereferee
 * src/ -- The Dereferee library source code
 * tests/ -- A set of unit tests that can be used to verify Dereferee proper
       behavior on various platforms


BUILDING DEREFEREE
==================
You can use Dereferee either by including its source tree directly in the build
process of your project, or separately compile it into a static library that
can then be linked into your project.

The Dereferee kit contains GNU makefiles for Cygwin, Mac OS X, and Unix
platforms, and nmake makefiles for Visual C++. The makefile targets are as
follows:

 * all: build all of the following targets
 * listener: compile LISTENER into an object file
 * platform: compile PLATFORM into an object file
 * libs: compile the Dereferee library source and link it into a static library.
       You can specify LISTENER and PLATFORM variables on the command line to
       pre-link one of the listener modules into the library. If this variable
       is left undefined, then the GNU makefiles will omit the listener from
       the library; the Visual C++ makefiles will include msvc_win32_platform
       and msvc_debugger_listener by default
 * tests: compile the test suite and link it into a run-tests executable
 * demo: compile the demo and link it into a run-demo executable (LISTENER
       and PLATFORM variables apply here)

The value of the LISTENER variable described above is the name of the listener
module object file, without a path or extension. The PLATFORM variable is
likewise the name of a platform module object file, without a path or
extension.

You can also use the run-tests and run-demo targets to automatically run the
respective executable after it has been built.

For example, to build libdereferee.a on Mac OS X, you can run

	make libs PLATFORM=gcc_macosx_platform LISTENER=stdio_listener

This will generate libdereferee.a in the Dereferee/build directory. In Microsoft
Visual C++, you can run

	nmake /F Makefile.mak libs PLATFORM=msvc_win32_platform LISTENER=msvc_debugger_listener
	
This will generate Dereferee.lib in the Dereferee/build-msvc directory. This
library is compiled with the /MDd option to use the multithreaded debug DLL
version of the Visual C++ CRT.


USAGE
=====

To use Dereferee in your own code, you must first prepare your project:

 1) Place the Dereferee/include directory in your project's include path.

 2) Include <dereferee.h> in any source or header file that will declare or use
    checked pointers.

 3) Link to the Dereferee static library.

 4) Use checked(T*) in lieu of T* when declaring pointers to heap memory in
    your code. This applies to variables, function arguments, and fields
    inside structures and classes.

    NOTE: Since in C++, the declaration "const T*" means "a pointer to a
    const object of type T", the analogous Dereferee declaration is
    checked(const T*), NOT const checked(T*). The following table shows the
    correct translations:

    Original declaration        Dereferee declaration
    ===================================================================
    T*                     -->  checked(T*)
    const T*               -->  checked(const T*)
    T* const               -->  checked(T* const)
    const T* const         -->  checked(const T* const)
    ===================================================================

    In other words, "checked" wraps the entire pointer type, including
    const modifiers.

The main design goal of the Dereferee kit was transparency; since this code
was designed to be used by students who are just learning C++, it was highly
desirable to make the functionality as unobtrusive as possible. Through C++
templates, operator overloading, and a minimal set of preprocessor macros,
the Dereferee kit provides the checked(T*) syntax that can be used almost
anywhere that a standard C++ pointer could be used. Only the declaration of
the pointer need change; almost any other usage of the pointer (including
calls to new/delete) will work as expected without modification.


KNOWN ISSUES
============

 * Dereferee only monitors pointers to memory allocated with the C++ "new" or
"new[]" operator. An attempt to set a checked pointer to point to any other
memory (such as stack space or otherwise dynamically allocated memory, for
instance malloc) will cause Dereferee to log an error at runtime. This
behavior is currently by design and is unlikely to change.

 * There may still be one or two lingering issues with compilation ambiguities
caused by the checked pointer template in more complex use cases. We have
created what we believe to be a reasonably comprehensive suite of test cases to
try to verify that all of the usual C++ constructs work with checked pointers
substituted for raw pointers. However, it is possible that we have missed
something. Any input from users with a great deal of expertise in esoteric C++
template constructs is always welcome.


ACKNOWLEDGMENTS
===============
This code is based on and extends the work done by Scott M. Pike and
Bruce W. Weide at The Ohio State University and Joseph E. Hollingsworth of
Indiana University SE in "Checkmate: Cornering C++ Dynamic Memory Errors
With Checked Pointers", Proc. of the 31st SIGCSE Technical Symposium on
CSE, ACM Press, March 2000.
