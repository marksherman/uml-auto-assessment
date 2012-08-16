#include <stdlib.h>

/*
 * Checks for the existence of a function that bfd needs to link properly.
 * This function may be in glibc on some systems, or it may require
 * libiberty, so first try compiling without it, then try compiling with
 * -liberty if that fails. If this second part fails, the user probably
 * needs to install libiberty from the appropriate package.
 */

struct objalloc;
struct objalloc *objalloc_create(void);

int main()
{
	objalloc_create();
	return 0;
}
