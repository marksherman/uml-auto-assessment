/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Malloc up a space for a 1 dimensional array of n intergers   */
/*                                                                           */
/* Approximate Completion Time : 10 minutes                                  */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>

void fill_array( int* ptr , int elements ) ;

int sum_array( int* ptr , int elements ) ;

int main( int argc , char* argv[] ) {

	int sum , memspace ;
	int* ptr ;

	printf( "Enter the number of numbers to be stored in the array\n" ) ;
	scanf( "%d" , &memspace ) ;
	/* create array of ints, user decides how big it is */
	ptr = ( int* ) malloc ( memspace * sizeof ( int ) ) ;
	fill_array( ptr , memspace ) ;
	sum = sum_array( ptr , memspace ) ;
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
	
