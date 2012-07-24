/***********************************/
/*Programmer: John Cavalieri	   */
/* Program :  malloc 2d array	   */
/*Completion time:	40min	   */
/***********************************/

#include<stdio.h>
#include<stdlib.h>

int main( int argc , char* argv[] ){

	int row,colm,*ptr,i,j;
	int row_ident,colm_ident;
	int sum = 0;

	printf( "\nEnter row length then column length\n" );
	
	scanf( "%d%d", &row, &colm);

	ptr = (int*) malloc( (row*colm)*sizeof(int) );

	printf( "Enter %d values to populate array\n" , row*colm );

	for ( j = 0 ; j < row ; j++ ){
		for ( i = 0 ; i < colm ; i++ ){
			scanf( "%d" , &ptr[j * colm + i] );
		}
	}

	
	printf( "Which row between row 0 and row %d to sum?\n", row-1 );
	
	scanf( "%d" , &row_ident );

	for ( i = 0 ; i < colm ; i ++ )
		sum += ptr[row_ident * colm + i ];

	printf( "\tThe sum of row %d is: %d\n", row_ident, sum );
	putchar('\n');

	printf( "Which column between 0 and %d to sum?\n", colm-1 );
	
	scanf( "%d" , &colm_ident );

	sum = 0;
	
	for ( j = 0 ; j < row ; j++ )
		sum += ptr[(j * colm + colm_ident)];

	printf( "\tThe sum of column %d is: %d\n", colm_ident, sum );
	
	sum = 0;

	for ( j = 0 ; j < row ; j++ ){
		for ( i = 0 ; i < colm ; i++ ){
			
			sum += ptr[j * colm + i];
		}
	}
	putchar('\n');

	printf( "\tThe sum of the entire matrix is %d\n", sum );
	
	free(ptr);

	return 0;
}
