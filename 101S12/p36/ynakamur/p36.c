/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 36: Persistence of a Number                                       */
/*                                                                           */
/* Approximate completion time: 25 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int persistence( int n );

int main( int argc, char *argv[] ) {

	int input, output;

	printf( "Please input integers to find their persistence.\n" );

	while( scanf( "%d", &input ) != EOF ) {
		output = persistence( input );
		printf( "%d\n", output );

	}

	return 0;

}


int persistence( int n ) {

	int x = 1, count = 0;
       
	while( n > 9 ) {
		do {
			x = x * ( n % 10 );
			n = n / 10;
		}

		while( n > 0 );
		n = x;
		x = 1;
		count++;
	}	
	
	return count;

}
