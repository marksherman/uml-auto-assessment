#include <bfd.h>

/*
 * Checks for the existence of libbfd on the system. Link with -lbfd.
 */
int main()
{
	bfd_openr(0, 0);
	return 0;
}
