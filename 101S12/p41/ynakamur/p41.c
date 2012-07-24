/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 41: Malloc up Space for a 1-Dimensional Array of n integers       */
/*                                                                           */
/* Approximate completion time: 10 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char *argv[] ) {

	int n, i, sum = 0;
	int *ptr;

	printf( "Please enter a value for n.\n" );

	scanf( "%d", &n );

	ptr = ( int* )malloc( n * sizeof( int ) );

	printf( "Please enter n integer values.\n" );

	for( i = 0; i < n; i++ ) {
		scanf( "%d", &ptr[i] );
	}

	for( i = 0; i < n; i++ ) {
		sum = sum + ptr[i];
	}

	printf( "The sum is equal to: %d\n", sum );

	free( ptr );

	return 0;

}
