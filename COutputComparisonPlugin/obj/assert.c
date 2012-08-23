#include <assert.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>

#ifdef __STDC__
#define _HAVE_STDC
#endif

#ifdef _HAVE_STDC
#define _PTR        void *
#define _AND        ,
#define _NOARGS     void
#define _CONST      const
#define _VOLATILE   volatile
#define _SIGNED     signed
#define _DOTS       , ...
#define _VOID void
#ifdef __CYGWIN__
#define _EXFUN(name, proto)     __cdecl name proto
#define _EXPARM(name, proto)        (* __cdecl name) proto
#else
#define _EXFUN(name, proto)     name proto
#define _EXPARM(name, proto)        (* name) proto
#endif
#define _DEFUN(name, arglist, args) name(args)
#define _DEFUN_VOID(name)       name(_NOARGS)
#define _CAST_VOID (void)
#ifndef _LONG_DOUBLE
#define _LONG_DOUBLE long double
#endif
#ifndef _PARAMS
#define _PARAMS(paramlist)      paramlist
#endif
#else
#define _PTR        char *
#define _AND        ;
#define _NOARGS
#define _CONST
#define _VOLATILE
#define _SIGNED
#define _DOTS
#define _VOID void
#define _EXFUN(name, proto)     name()
#define _DEFUN(name, arglist, args) name arglist args;
#define _DEFUN_VOID(name)       name()
#define _CAST_VOID
#define _LONG_DOUBLE double
#ifndef _PARAMS
#define _PARAMS(paramlist)      ()
#endif
#endif

#ifdef __GNUC__
#define _ATTRIBUTE(attrs) __attribute__ (attrs)
#else
#define _ATTRIBUTE(attrs)
#endif

#ifdef __cplusplus
#if !(defined(_BEGIN_STD_C) && defined(_END_STD_C))
#ifdef _HAVE_STD_CXX
#define _BEGIN_STD_C namespace std { extern "C" {
#define _END_STD_C  } }
#else
#define _BEGIN_STD_C extern "C" {
#define _END_STD_C  }
#endif
#endif
#else
#define _BEGIN_STD_C
#define _END_STD_C
#endif

/*
void
_DEFUN (__assert, (file, line, failedexpr),
 const char *file _AND
 int line _AND
 const char *failedexpr)
{
  (void)fiprintf(stderr,
  "\nhint: assertion \"%s\" failed: file \"%s\", line %d\n",
    failedexpr, file, line);
  abort();
  /* NOTREACHED *//*
}

void
abort()
{
    raise (SIGABRT);
}
*/
int
_system_r (ptr, s)
     struct _reent *ptr;
     _CONST char *s;
{
  (void)fiprintf(stderr,
  "\nhint: system calls are not allowed: \"%s\"\n",
  s);
  abort();
  /* NOTREACHED */
  return 1;
}

int
system (s)
     _CONST char *s;
{
  (void)fiprintf(stderr,
  "\nhint: system calls are not allowed: \"%s\"\n",
  s);
  abort();
  /* NOTREACHED */
  return 1;
}

int
_DEFUN (_execve_r, (ptr, name, argv, env),
     struct _reent *ptr _AND
     char *name _AND
     char **argv _AND
     char **env)
{
  (void)fiprintf(stderr,
  "\nhint: system calls are not allowed: \"%s\"\n",
  name);
  abort();
  /* NOTREACHED */
  return 1;
}

int
_DEFUN (_execve, (name, argv, env),
     char *name _AND
     char **argv _AND
     char **env)
{
  (void)fiprintf(stderr,
  "\nhint: system calls are not allowed: \"%s\"\n",
  name);
  abort();
  /* NOTREACHED */
  return 1;
}

int _fork()
{
  (void)fiprintf(stderr,
  "\nhint: forking is not allowed\n");
  abort();
  /* NOTREACHED */
  return -1;
}
