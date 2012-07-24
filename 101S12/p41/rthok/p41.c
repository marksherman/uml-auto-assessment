/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 41: Malloc up Space for a 1- Dimensional Array of n Integers   */
/*                                                                        */
/* Approximate Completion Time: 35 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>
#include <stdlib.h>


int main( int argc, char *argv[] ) {

	int n = 0 , i = 0 , sum = 0 ;

	int* array ; 

	printf( "\nEnter a length for an array: ") ;

	scanf( "%d", &n);

	array = ( int* )malloc( n * sizeof( int ));

	printf( "%d numbers are need.\n\n", n ) ;

		while( i < n ){

			printf( "Enter a number for array[%d]: ", i ) ;

       			scanf( "%d", &array[i]);
		
			sum = array[i] + sum ;

			i++ ;
		}

	printf( "\nThe sum of all the elements of the array is %d.\n\n", sum ) ; 

	free( array ) ;

  return 0 ;

}
