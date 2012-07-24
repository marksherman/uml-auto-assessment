
/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 37 : Digit Sum (Again)                                         */
/*                                                                        */
/* Approximate Completion Time: 20 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int digitsum(int x);

int main( int argc, char *argv[] ) {

	FILE* fin;
	int x ;
	int i = 1 ;
	int sum ;   
	
	fin = fopen( argv[i], "r" ) ;
	
	while( fscanf( fin,"%d", &x )!= EOF ) {

		sum = digitsum( x ) ;

		printf( "\nThe digitsum of %d is %d.\n", x , sum );   

	}

	printf( "\n") ;

	fclose( fin ) ;

	return 0 ;

}

int digitsum( int x ){

	int y = 0 , z = 0 ;

	while( x >= 10 ){

		y = x % 10 ;

		x = x / 10 ;

		z = z + y ;

      	}

	z = z + x ;

	return z ;
	
}
