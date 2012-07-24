
/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 36: Persistence of a Number                                    */
/*                                                                        */
/* Approximate Completion Time: 90 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int per( int num );

int main( int argc, char *argv[] ) {

	int num = 0 ; int count;

	printf( "\nEnter a number: " ) ;

	while( scanf( "%d", &num ) != EOF ){

		count = 0 ;

		printf( "The persistence of %d", num ) ;

 		while( num >= 10 ){

			num = per( num ) ;

			count ++ ;

		}

		printf( " is %d.\n", count ) ;

		printf( "\nEnter another number or an EOF: " ) ;
	
	}
	
	printf( "\n\n" ) ;
 
	return 0 ;
	
}

int per( int num ){

	int x = 0, y = 1 ;

	while( num >= 10 ){

		x = num % 10 ;

		num = num / 10 ;

		y = x * y ;

	}
	
	return( num * y) ;

}
