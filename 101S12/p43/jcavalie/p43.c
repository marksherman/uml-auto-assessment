/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : Square deal	   */
/*Completion time:	1 day	   */
/***********************************/

#include<stdio.h>
#include<stdlib.h>

int* ptr;
/*Global pointer to array in heap*/
int N, init;
int even = 1, odd =0;
/*Even & odd used to generate odd
 *and even integer sequences*/

void matrix_final( int row, int colm);
void matrix_manip( int row, int colm, int count );
int isprime( int num );

int main( int argc , char* argv[] ){
	int r , c, i, j;
	/*r for row & c for column*/
	const int counter = 0;
      

	printf( "Enter size of square matrix\n");
	scanf( "%d" , &N);

	printf( "\nEnter intial value\n");
	scanf( "%d" , &init);

	putchar('\n');
	ptr = (int*)malloc( N*N*sizeof(int) );

	r = N/2;
	c = N/2;
	/*Variables that identify middle
	 *of matrix.*/

	ptr[ r * N + c ] = init;
	/*Places initial value in
	 *middle of matrix*/

	matrix_manip( r , c, counter );
	
	printf( "\tA %d by %d matrix of primes and ***'s:\n\n" , N, N);

	for ( i = 0 ; i < N ; i++){
		for ( j = 0 ; j < N ; j++ ){
			if ( isprime(ptr[ i * N + j ]) != 1)
			printf( "%-4d" , ptr[ i * N +j ] );

			else printf( "%-4s", "***" );
			printf("%-2c",' ');
		}
		
		printf( "%-1c", '\n');
	}

	free(ptr);

	return 0;
}
	
void matrix_manip( int row, int colm, int count ){

	int i;

	
	
	/*Right moves occur in increasing odd sequences
	 *i.e. 1,3,5,7...*/
	for ( i = 0 ; i < (2 * odd + 1) ; i++ ){
		ptr[ row * N + (colm = colm + 1) ] = ++init;
		count++;
		if ( count == (N*N - 3*N + 2) ){
			/*Identifies remaining 3*N-3 moves
			 *which are handled by matrix_final function*/
			matrix_final( row, colm );
			
			return;
		}
	}

	/*Up moves occur in increasing odd sequences*/
	for ( i = 0 ; i < (2 * odd + 1) ; i++ ){
		ptr[ (row = row - 1) * N + colm ] = ++init;
		count++;
		if ( count == (N*N - 3*N + 2) ){
			matrix_final( row, colm );
			return;
		}
	}

	/*Left moves occur in increasing even sequences
	 *i.e. 2,4,6,8...*/
	for ( i = 0 ; i < (2* even) ; i++ ){
		ptr[ row * N + (colm = colm - 1) ] = ++init;
		count++;
		if ( count == (N*N - 3*N + 2) ){
			matrix_final( row, colm );
			return;
		}
	}

	/*Down moves occur in increasing even sequences*/
	for ( i = 0 ; i < (2 * even) ; i++ ){
		ptr[ (row = row+1) * N + colm ] = ++init;
		count++;
		if ( count == (N*N - 3*N + 2) ){
			matrix_final( row, colm );
			return;

		}
	}
	
	even++;
	odd++;
	
	return matrix_manip( row, colm , count); 
	/*Recursive until 3*N-3 moves remaining*/
}

void matrix_final( int row, int colm ){

	int i;
	
	/*Final left movement*/
	for ( i = 0 ; i < N -1 ; i++ ){
		ptr[ row * N + (colm = colm - 1) ] = ++init;
	}

	/*Final down movement*/
	for ( i = 0 ; i < N -1 ; i++ ){
		ptr[ (row = row+1) * N + colm ] = ++init;

	}
	
	/*Final right movement*/
	for ( i = 0 ; i < N -1 ; i++ ){
		ptr[ row * N + (colm = colm + 1) ] = ++init;

        }
       
	return;
}

int isprime( int num ){

	int i;
	
	if ( num == 1 )
		return 1;
     

	for( i = 2 ; i < num ; i++ ){
		if ( num % i == 0 )
			return 1;
	}

	return 0;
	/*Returing 0 indicates a prime number*/
}
