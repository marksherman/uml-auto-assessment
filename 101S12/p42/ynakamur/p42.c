/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 42: Malloc up Space for a Two-Dimensional Array                   */
/*                                                                           */
/* Approximate completion time: 30 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char *argv[] ) {

	int array_size, r, c, i, j, input, sum = 0;
	int *ptr;

	printf( "Please input an array size.\n" );

	scanf( "%d", &array_size );

	r = array_size;
	c = array_size;

	ptr = ( int* ) malloc( r * c * sizeof ( int ) );

	printf( "Please input %d integers to populate the array.\n", r * c );

	for( i = 0; i < c; i++ ) {
		for( j = 0; j < r; j++ ) {
			scanf( "%d", &ptr[ i * c + j ] );
		}
	}

	printf( "Which row would you like to be summed? ( 0 to %d )\n", r - 1 );

	scanf( "%d", &input );

	for( j = 0; j < array_size; j++ ) {
		sum = sum + ptr[ input * array_size + j ];
	}

	printf( "%d\n", sum );

	sum = 0;

	printf( "Which column would you like to be summed? ( 0 to %d )\n", c - 1 );

	scanf( "%d", &input );

	for( i = 0; i < array_size; i++ ) {
		sum = sum + ptr[ i * array_size + input ];
	}

	printf( "%d\n", sum );

	sum = 0;

        for( i = 0; i < c; i++ ) {
                for( j = 0; j < r; j++ ) {
                        sum = sum + ptr[ i * c + j ];
                }
        }

      
	printf( "The total sum of the entire array is equal to: %d\n", sum );

	free( ptr );

	return 0;

}
