/*************************************************************************/
/* Programmer:   Dylan Bochman                                           */
/* Program 42:   Malloc Up A Two Dimensional Array                       */
/* Time:         115 minutes                                             */ 
/*************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main ( int argc, char *argv[] ) {

    int *x, c, r, i, j, rs, cs, rsum = 0, csum = 0, sum = 0 ;

    printf ( "\nPlease enter the number of columns in the array: " ) ;
    scanf ( "%d", &c ) ;
    printf ( "\nPlease enter the number of rows in the array: " ) ;
    scanf ( "%d", &r ) ;

    x = ( int * ) malloc ( sizeof ( int ) * c * r ) ;
      
    printf ( "\nPlease enter %d numbers to populate the array:\n" ,c*r ) ;
    
    for ( i = 0 ; i < c ; i++ ) {
	for ( j = 0 ; j < r ; j++ ) {
	    scanf("%d", &x[j + c * i] );
	}
    }

    printf ( "\nPlease select a row # to sum: " ) ;
    scanf ( "%d" , &rs );
   
    for ( j = 0 ; j < r ; j++ ) {
	rsum += x[rs * c + j];
    }

    printf ( "\nThe sum of row %d is: %d\n", rs, rsum ) ;
    printf ( "\nPlease select a column # to sum: " ) ;
    scanf ( "%d" , &cs );

    for ( i = 0 ; i < r ; i++ ) {
	    csum += x[cs * c + i];
	}
    
    printf ( "\nThe sum of the selected column is: %d\n", csum ) ;

    for ( i = 0 ; i < c * r ; i++ ) {
	    sum += x[i] ;
	}
    
    printf ( "\nThe sum of the entire array is %d.\n\n", sum ) ;  

    free ( x );
 
    return 0 ; 
}
