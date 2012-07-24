/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 39: Recursive Persistence                                         */
/*                                                                           */
/* Approximate completion time: 30 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

void EOF_scan( int num );
int persistence( int input );
int reducer( int input );

int main( int argc, char *argv[] ) {

	int input;

	printf( "Please input integers until EOF is reached.\n" );

	scanf( "%d", &input );

	EOF_scan( input );

	return 0;

}


void EOF_scan( int num ) {

	int output;

	if( num != EOF ) {
		output = persistence( num );
		printf( "%d\n", output );
		scanf( "%d", &num );
		return EOF_scan( num );
	}

	else {
		return;
	}

}


int persistence( int input ) {

	if( input < 10 ) {
		return 0;
	}

	else {
		int digit = reducer( input );
		return 1 + persistence( digit );
	}

}


int reducer( int input ) {

	if( input < 10 ) {
		return input;
	}

	else {
		return( input % 10 ) * reducer( input / 10 );
	}

}

  
