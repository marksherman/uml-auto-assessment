/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 35:   Passing a Two Deminsional Array        */
/* Time:         10 minutes                             */
/********************************************************/

#include <stdio.h>
int sum ( int x[][3], int r, int c );
int main ( int argc, char *argv[] ) {

    int x[3][3], i, j, ans ; 

    printf ( "\nPlease enter 9 values: " ) ; 

    for ( i = 0 ; i < 3 ; i++ ) {

	for ( j = 0 ; j < 3 ; j++ ) {

	    scanf ( "%d", &x[i][j] ) ;
    
	}
    }

    ans = sum ( x, 3, 3 ) ;

    printf ( "\nThe sum of the values in the array is %d.\n\n", ans ) ;
  
    return 0 ;
}

int sum ( int x[][3], int r, int c ) {

    int sum = 0, i, j ; 

    for ( i = 0 ; i < r ; i++ ) {

	for ( j = 0 ; j < c ; j++ ) {

	    sum = sum + x[i][j] ;

	}

    }

    return sum ; 
}
