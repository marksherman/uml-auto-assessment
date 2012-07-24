/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 32:   Non-recursive Factorial                */
/* Time:         25 minutes                             */
/********************************************************/
#include <stdio.h>
int factorial( int n );
int main ( int argc, char*argv[] ) {
    int n , y;
    printf ( "\nPlease enter an integer:\n" ) ;
    scanf ( "%d", &n );
    y = factorial( n );
    printf ( "The factorial of %d is %d", n, y ) ;
    printf ( "\n\n" ) ;  
    return 0 ;
}
int factorial ( n ) {
    int i, result;
    result = 1;
    for(i = 1; i <= n; i++){
	result = result*i;
    }
    return result ;
}

