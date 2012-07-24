/*************************************/
/* Programmer: Alexander Gonzalez    */
/*                                   */
/* Assignment: Square Deal           */
/*                                   */
/* Completion: 30 min                */
/*************************************/

#include <stdio.h>
#include <stdlib.h>

int IsPrime( int n );

int main(int argc, char* argv[]) {

    int n, i, j, k;

    int arr[50][50];

    printf( "Enter an odd integer n between 3 and 15:\n " ) ;
    scanf( "%d", &n ) ;

    printf( "Enter an initial value:\n " ) ;
    scanf( "%d", &i ) ;

    for( j = 0 ; j < n ; j++ ) {
	
	for( k = 0 ; k < n ; k++ ) {
	    if( IsPrime( i++ ) == 1 )
		printf("%8d", i) ;
	    else
		printf( "%8s", "***" ) ;
	}
	printf("\n");
	arr[j][k] = arr[j+1][k+1] ;
    }
    return 0;
}
int IsPrime( int n )
{
    int i, count = 0 ;
    for( i = 1 ; i <= n ; i++ )
	{
	    if( ( n % i ) == 0 ) count++ ;
	}
    return ( count == 2 ) ;
}
