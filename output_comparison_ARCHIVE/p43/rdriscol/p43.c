/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program:p43 Square Deal         */
/*                                 */
/* Approx Completion Time: Days    */
/***********************************/

#include <stdio.h>
#include <stdlib.h>

int check_prime( int x );

int main( int argc, char* argv[]){

	int j,k,n,i,c, l;
	int initial_value, start;
	int* array;
	

	printf( "Please enter a value:\n " );
	scanf( "%d", &n );
	printf( "\nPlease enter another value greater than zero:\n ");
	scanf( "%d", &initial_value );

	array = (int*)malloc(n*n*sizeof(int)); 
	j=k=n/2;
	array[ j * n + k ] = start = initial_value;
	
	for( i = 1; i < n; ++i ){
		if( i % 2==1 ){
			for( c = 0; c < i; ++c ){
				array[ j * n + (++k) ] = ++start;/*right*/
			}
			for( c = 0; c < i; ++c ){
				array[ (--j)* n + k ] = ++start;/*up*/
			}
		}
		if( i % 2==0 ){
			for( c = 0; c < i; ++c ){
				array[ j * n + (--k) ] = ++start;/*down*/
			}
			for( c = 0; c < i; ++c ){
				array[ (++j) * n + k ] = ++start;/*left*/
			}
		}
	}
	for( i = 0; i < n; ++i )
		array[ j * n + (++k) ] = ++start;

	for( i = 0; i < n; ++i ) {
		for ( l = 0; l < n; ++l )
			if( check_prime( array[i * n + l] ) == 0 ) {
				printf( " *** " );
			} else {
				printf( " %3d ", array[i * n + l] );
			}
		putchar( '\n' );
	}
	
	return 0;

}

int check_prime( int x ) 
{
	int c;
	for ( c = 2; c <= x - 1; c++ )
	{
		if( x % c == 0)
			return 0;
	}	
	      
	return 1;
		
}

