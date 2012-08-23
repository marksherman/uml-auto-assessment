#include <libintl.h>

/*
 * Checks for the existence of libintl on the system. First attempt to
 * link *without* -lintl since some versions of glibc come with these
 * functions built-in. If this fails, then try linking *with* -lintl.
 */
int main()
{
	/* This function is referenced by libbfd. */
	libintl_dgettext(0, 0);
	return 0;
}
