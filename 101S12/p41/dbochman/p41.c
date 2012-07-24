/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 41:   Malloc 1-D Array of n integers summed  */
/* Time:         20 minutes                              */
/********************************************************/
#include <stdlib.h>
#include <stdio.h>
int main ( int argc, char *argv[] ) {
    int i, j , n , sum = 0;
    printf ( "\nPlease enter the # of integers you would like to sum:\n" );
    scanf ( "%d", &n );
    if ( n <= 0 ){
	printf ( "\nThe sum of zero intergers is zero\n" );
    }
    else {
	int *numbers = malloc(n * sizeof(int) );
	printf ("\nPlease enter %d intergers to sum: " ,n );   
	for ( i = 0 ; i < n ; i++ ) {
	    scanf ("%d", &numbers[i] ) ;
	}
	for ( j = n-1 ; j >= 0 ; j-- ) {
	    sum += numbers[j];
	}
	printf ( "\nThe sum of the %d numbers entered is %d\n\n", n , sum);
    }
    return 0 ;
  
}
