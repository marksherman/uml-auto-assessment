/************************************************/
/* Programmer: Kyle White                       */
/* Program  42: Malloc Space for a 2-D Array    */
/* Approximate completion time: 30 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>
#include <stdlib.h>

int r,c;

int column_sum ( int* arr_ptr, int column );
int row_sum ( int* arr_ptr, int row );
int total_sum ( int* arr_ptr );
int main (int argc, char* argv [])

{

	int j=0,i=0,totsum=0,row=0,rowsum=0,column=0,colsum=0;
	int* arr_ptr;

	printf ( "Enter a number of rows: " );

	scanf ( "%d", &r );

	printf ( "Enter a number of columns: " );

	scanf ( "%d", &c );

	arr_ptr = ( int* ) malloc ( r * c * sizeof ( int ) );

	printf ( "\nNow enter numbers to fill each row of the array\n" );

	for ( j=0 ; j<r ; j++ ){

		printf ( "Row %d: ", j );

		for ( i=0 ; i<c ; i++ ){			

			scanf ( "%d", &arr_ptr[(j*r+i)] );

		}

	}

	printf ( "Enter a row between 0 and %d to be summed: ", r-1 );

	scanf ( "%d", &row );

	rowsum = row_sum ( arr_ptr, row );

	printf ( "The sum of row %d is: %d", row,rowsum );

	putchar ( '\n' );

	printf ( "Enter a column between 0 and %d to be summed: ", c-1 );

	scanf ( "%d", &column );

	colsum = column_sum ( arr_ptr , column );

	printf ( "The sum of column %d is: %d", column, colsum );

	putchar ( '\n' );

	totsum = total_sum ( arr_ptr );

	printf ( "The total sum of the array is: %d\n", totsum );

	free ( arr_ptr );

	return 0;
  
}

int row_sum ( int* arr_ptr, int row )

{

	int i,sum=0;

	for ( i=0 ; i<c ; i++ ){

		sum += arr_ptr[row*c+i];

			}

	return sum;

}

int total_sum ( int* arr_ptr )

{

	int i,sum=0;

	for ( i=0 ; i<(r*c) ; i++ ){

		sum += arr_ptr[i];

	}

	return sum;

}

int column_sum ( int* arr_ptr, int column )

{

	int i=0,sum=0;

	for ( i=0 ; i<r ; i++ ){

		sum += arr_ptr[i*r+column];

	}

	return sum;

}
