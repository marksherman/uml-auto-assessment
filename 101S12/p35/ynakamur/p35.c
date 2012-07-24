/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 35: Two Dimensional Array                                         */
/*                                                                           */
/* Approximate completion time: 20 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int sum( int size[][3] );

int main( int argc, char *argv[] ) {

	int result, array[3][3], i, j;

	printf( "Please input 9 integer values.\n" );

	for( i = 0; i < 3; i++ ) {
		for( j = 0; j < 3; j++ ) {
			scanf( "%d", &array[i][j] );
		}
	}

	result = sum( array );

	printf( "%d\n", result );

	return 0;

}


int sum( int size[][3] ) {

	int i, j, result = 0;

	for( i = 0; i < 3; i++ ) {
		for( j = 0; j < 3; j++ ) {
			result += size[i][j];
		}
	}

	return result;

}
