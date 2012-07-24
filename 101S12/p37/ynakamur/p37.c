/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 37: Digit Sum (again)                                             */
/*                                                                           */
/* Approximate completion time: 20 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int digitsum( int n );
int main( int argc, char *argv[] ) {

	int number, sum;
	FILE* fin;
  
	fin = fopen( argv[1], "r" );
	
	while( fscanf( fin, "%d", &number ) != EOF ) {
		sum = digitsum( number );
		printf( "%d\n", sum );
	}

	fclose( fin );

	return 0;
		
	}


int digitsum( int n ) {
		
	int sum = 0;

	while( n > 0 ) {
		sum += n % 10;
		n = n / 10;
	}

	return sum;

}
