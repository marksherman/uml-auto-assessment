/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Malloc up a space for a 2 dimensional array                  */
/*                                                                           */
/* Approximate Completion Time : 25 minutes                                  */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>

void fill_array( int* ptr , int elements ) ;

int sum_row( int* ptr , int startelement , int col ) ;

int sum_column( int* ptr , int startelement , int col ) ;

int sum_array( int* ptr , int elements ) ;

int main( int argc , char* argv[] ) {

	int sum , row , col , startelement ;
	int* ptr ;

	printf( "Enter the number of rows then then columns\n" ) ;
	scanf( "%d%d" , &row , &col ) ;
	/* create array of ints, user decides how big it is */
	ptr = ( int* ) malloc ( row * col * sizeof ( int ) ) ;
	fill_array( ptr , row * col ) ;
	/* gets sum of user picked row and prints */
	printf( "Which row would you like summed?\n" ) ;
	scanf ( "%d" , &startelement ) ;
	sum = sum_row( ptr , startelement , col ) ;
	printf ( "The sum of your row is %d\n" , sum ) ;
	/* gets sum of user picked column and prints */
	printf ( "Which columns would you like summed?\n" ) ;
	scanf ( "%d" , &startelement ) ;
	sum = sum_column( ptr , startelement , col ) ;
	printf ( "The sum of your column is %d\n" , sum ) ;
	/* gets sum of array and prints */
	sum = sum_array( ptr , row * col ) ;
	printf( "The sum of the elements in your array is %d\n" , sum ) ;
	free ( ptr ) ;
	return 0 ;
}

/* function will fill an array of any size as long as this array has been 
   created using malloc prior to the call to this function. Takes ptr to array,
   and the number of elements in the array and uses loop to fill.
*/
void fill_array( int* ptr , int elements ) {
	
	int i ;
	
	printf( "Enter the numbers you wish to store in the array\n" ) ;
	for ( i = 0 ; i < elements ; i++ ) 
		scanf( "%d" , &ptr[i] ) ;
	
}

/* Function will sum an array of any size.  Takes ptr to array and # of 
   elements then runs loop to add up each element.
*/		       
int sum_array( int* ptr , int elements ) {
	
	int i , sum = 0 ;
	
	for ( i = 0 ; i < elements ; i++ ) 
		sum += ptr[i] ;
	return sum ;
}

/* Takes array and depending on what row the user picked will sum all the 
   values stored in each element in that row.  
*/
int sum_row( int* ptr , int startelement , int col ) {
	
	int i , sum = 0 ; 
	for ( i = 0 ; i < col ; i++ ) 
		sum += ptr[ ( ( startelement - 1 ) * col ) + i ] ;
	return sum ;
}

/* Takes array and depending on what column the user picked will sum all the 
   values stored in that column.
*/
int sum_column( int* ptr , int startelement , int col ) {
	
	int i , sum = 0 ;
	
	for ( i = 0 ; i < ( col * col )  ; i += col ) 
		sum += ptr[ ( ( startelement - 1 ) * col ) + i ] ;
	return sum ;
}
