/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 42: Malloc up Space for a Two- Dimensional Array               */
/*                                                                        */
/* Approximate Completion Time: 75 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char *argv[] ) {


	int* value ;
	int r , c , i , j , x , y ;
	int sum1 = 0 , sum2 = 0 , sum3 = 0, z = 1 ;

	printf( "\nEnter the numbers of rows: " );
	
	scanf( "%d", &r ) ;

	printf( "Enter the numbers of columns: " ) ;
	
	scanf( "%d", &c ) ;

	value = (int*) malloc( r * c * sizeof( int ) ) ;

	printf( "\nEnter %d numbers: \n\n", r * c ) ; 
	
	for( i = 0 ; i < r ; i++ ){
		
		for( j = 0 ; j < c ; j++ ){

			printf("Input %d: ", z ) ;

			z++ ;

			scanf( "%d", &value[ i * r + j ] ) ;	
		
			sum3 += value[ i * r + j ];

		}

	}

	printf("\nWhich row do you want to sum up? (%d - %d): ", 0 , r - 1 ) ;
	
	scanf( "%d" , &x ) ;

	for( j = 0 ; j < c ; j++ ){
		
		sum1 += value[ x * c + j ] ;
	
	}

	printf( "The sum of row %d is %d." , x , sum1 ) ;
	
	printf( "\n\nWhich column do you want to sum up? (%d - %d): ", 0 , c - 1 ) ;
	
	scanf( "%d", &y ) ;

	for( i = 0 ; i < r ; i++ ){

		sum2 += value[ i * c + y ] ;
	
	}

	printf( "The sum of column %d is %d.", y , sum2 ) ;
	
	printf( "\n\nThe sum of the whole array is %d.\n\n", sum3 ) ;

	free( value );

  return 0 ;

}
