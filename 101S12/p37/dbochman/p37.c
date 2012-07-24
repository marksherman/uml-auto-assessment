/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 37:   Digit Sum Redux                        */
/* Time:         5 minutes                              */
/********************************************************/
#include <stdio.h>
int digitsum (int x);
int main ( int argc, char *argv[] ) {
    int x,y;
    FILE *fin = fopen ( argv[1], "r" ) ;
    printf ( "\n" ) ;
    while ( fscanf ( fin, "%d ", &x ) != EOF ){
        y = digitsum( x );
        printf ( "%d ",y ) ;
    }
    fclose ( fin ) ;
    printf ( "\n\n" ) ;
    return 0 ;
}

int digitsum ( x )
{
    int digit, sum = 0 ;
    while ( x > 0 ) {
	digit = x % 10 ;
	x = x / 10 ;
	sum = sum + digit ;
    }
    return sum ;
}

