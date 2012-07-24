/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 38:   Recursive Digit Sum                    */
/* Time:         5 minutes                              */
/********************************************************/
#include <stdio.h>
int digitsum ( int x ) {
    if (x < 10) 
	return (x);
    else
	return (x % 10 + digitsum(x / 10));
}
int main ( int argc, char *argv[] ) {
    int x ;
    FILE *fin = fopen ( argv[1], "r" ) ;
    printf ( "\n" ) ;
    while ( fscanf ( fin, "%d", &x ) != EOF ) 
	printf ( "%d ", digitsum (x) ) ;
    fclose ( fin ) ; 
    printf ( "\n\n" ) ;  
    return 0 ;
}
