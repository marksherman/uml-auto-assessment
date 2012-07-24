/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 43: Square Deal                                                */
/*                                                                        */
/* Approximate Completion Time: 240 minutes                               */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int prime( int num ) ;

void spiral ( int** grid , int num  , int value ) ;

int main( int argc, char *argv[] ) {

	int N , initial_value , value , i , j , k ;

	int** grid ; 

	printf( "\nEnter a size for the square: " ) ;

	scanf ("%d", &N ) ;

	printf( "\nEnter a initial value: " ) ;

	scanf( "%d", &initial_value ) ;

	grid = ( int** ) malloc( N * sizeof( int* ) ) ; 

		for( k = 0 ; i < N ; k++ ){

			grid[i] = ( int*) malloc( N * sizeof( int ) ) ;

		}

		spiral( grid , N , initial_value ) ; 

		for( i = 0 ; i < N ; i++ ){

			for( j = 0 ; j < N ; j++ ) {

				value = grid[i][j];

				value = prime( value ) ;

				if( value == -1 )

					printf( " *** " ) ;

					else

						printf( "  %d  " , grid[j][k] ) ; 

			}

			printf( "\n" ) ;

		}

		return 0 ;

}

void spiral( int** grid , int num , int value  ){ 
			
	int i , j , center , x , y , count ;

	center = num / 2 ;

	i = center ;

	j = center ;

	grid[i][j] = value ;  

	for( count = 1 ; count < num ; count++ ){ 

		for( x = 1 ; x <= count ; x++ ){
			
			i = i + x ;

			while( i < num ){
 
			value++ ;

			grid[i][j] = value ;

			}

		}
		
		for( y = 1 ; y <= count ; y++ ){ 
			
			j = j - y ;

			while( i < num ){

			value ++ ;

			grid[i][j] = value ;	       

			}

		}
		
		count++ ;

		for( x = 1 ; x <= count ; x++ ){

			i = i - x ;

			while( i < num ){

			value++ ;

			grid[i][j] = value ;

			}

		}

	       for( y = 1 ; y <= count ; y++ ) {

		       j = j + y ;

		       while( i < num ){

		       value ++ ;

		       grid[i][j]= value ;

		       }

	       }

	}


}

int prime( int num ){

	int i , a = 0 ;
	
	if( num <= 1 )

		return -1 ;

	for( i = 2 ; i <= num ; i++ ){

		a = num % i ;

		if( a == 0 )     
		       
			return -1 ;
		else
		
			return num ;
		
	}

	return -1 ;

} 


