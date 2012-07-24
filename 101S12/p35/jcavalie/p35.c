/***********************************/
/*Programmer: John Cavalieri	   */
/* Program :  passing 2d array	   */
/*Completion time:	20min	   */
/***********************************/

#include<stdio.h>

int sum( int vector[][3], int rowlngth, int colmlen );

int main( int argc , char* argv[] ){
	
	int matrix[3][3];
	int i,j,row,y,colmn;
	
	printf( "\nEnter sequence of 9 ints as vector components\n" );

	for ( i = 0 ; i < 3 ; i++ ){
		for ( j = 0 ; j < 3 ; j++ )
			scanf( "%d" , &matrix[i][j] );
	}
	row = i;
	colmn = j;

	y = sum( matrix , row, colmn  );

	printf( "\nSum of vector components: %d\n" , y );
	
	return 0;
}

int sum( int vector[][3], int rowlngth, int colmnlen ){
	
	int sum = 0;
	int i,j;

	for ( i = 0 ; i < rowlngth ; i++ ){
		for ( j = 0 ; j < colmnlen ; j++ )
			sum += vector[i][j];
	}

	return sum;
}
