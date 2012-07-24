/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 36:   Persistence of a Number                */
/* Time:         20 minutes                             */
/********************************************************/

#include <stdio.h>
int persist ( int x );
int main ( int argc, char *argv[] ) {

    int x ;   

    printf ( "\nPlease enter a number: " ) ;

    while ( scanf ( "%d", &x ) != EOF ) {

	printf ( "\nThe persistence of the number is %d.\n\n", persist ( x ) ) ;  

	printf ( "Please enter a number: " ) ;
  
    }    

    return 0 ; 
}
int persist ( int x ) {

    int i, carry, y ;

    y = x / 10 ;

    for ( i = 0 ; y != 0 ; i++ ) {

	for ( y = 1 ; x != 0 ; y *= carry ) {

	    carry = x % 10 ;

	    x /= 10 ;

	}

	x = y ;

	y = x / 10 ;

    }

    return i ;

}
