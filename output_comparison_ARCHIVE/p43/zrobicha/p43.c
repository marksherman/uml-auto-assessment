/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Square Deal                                                  */
/*                                                                           */
/* Approximate Completion Time : 3 hours                                     */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>

void fill_array( int* ary_ptr , int elements , int N ) ;

int prime_detect( int ary_prt ) ;

int main( int argc , char* argv[] ) {

	int* ary_ptr ;
	int N , initial_value , i , j , test ;

	printf( "Enter the size of the square\n" ) ;
	scanf( "%d" , &N ) ;
	printf( "Enter the starting value\n" ) ;
	scanf( "%d" , &initial_value ) ;
	
	/* make array of size user defines */
	ary_ptr = ( int* ) malloc ( N * N * sizeof( int ) ) ;
	
	fill_array( ary_ptr , initial_value , N  ) ;
	
	/* takes return of prime_detect. if number ! prime prints *** or if it
	   is prime prints the number */
	for( i = 0 ; i < N ; i++ ) {
		for( j = 0 ; j < N ; j++ ) {
			test = prime_detect( ary_ptr[ j + ( i * N ) ] ) ;
			if ( test == 1 ) 
				printf( "***" ) ;
			else
				printf( "%d" , ary_ptr[ j + ( i * N ) ] ) ;
			putchar ( '\t' ) ;
		}
		putchar ( '\n' ) ;
	}
	return 0 ;
}

/* fills array starting with number selected by user.  i tests for end and is
   added to user initial value to be put in array. direct is how many times it 
   moves in any directions. j is iterator for individual loops. l is what 
   address of array is being stored into. 
*/
void fill_array( int* ary_ptr , int elements , int N ) {
	
	int i = 1 , direct = 1 , j , l = ( N * N ) / 2 ;
	
	ary_ptr[l] = elements ;
	for (  ; i < N * N ; direct++ ) {
		for( j = 0 ; i <= N * N && j < direct ; j++ , i++ ) {
			l += 1 ;
			ary_ptr[l] = elements + i ;
		}
		for( j = 0 ; i <= N * N && j < direct ; j++ , i++ ) {
			l -= N ;
			ary_ptr[l] = elements + i ;
		}
		direct++ ;
		for ( j = 0 ; i <= N * N && j < direct ; j++ , i++ ) {
			l -= 1 ;
			ary_ptr[l] = elements + i ;
		}
		for ( j = 0 ; i < N * N && j < direct ; j++ , i ++ ) {
			l += N ;
			ary_ptr[l] = elements + i ;
		}
	}
}

/* tests to see if number is prime, returns true to main so main knows what to print */

int prime_detect( int ary_ptr ) {
	
	int i , test = 0 ;
	
	for( i = 2 ; i < ary_ptr ; i++ ) {
	       
		if ( ( ary_ptr % i ) == 0 ) 
			test = 1 ;
	}
       
	return test ;
}
	

		  
