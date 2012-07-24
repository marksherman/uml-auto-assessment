
/***********************************/
/*Programmer: John Cavalieri	   */
/* Program :  	malloc up 1D array */
/*Completion time:	16min	   */
/***********************************/


#include<stdio.h>
#include<stdlib.h>


int main( int argc, char* argv[]){

	int n,input,i,sum = 0;
	int* ptr;

	printf( "enter length of array\n" );

	scanf( "%d" , &n );
	
	ptr =(int*) malloc( n*sizeof(int));
	
	printf( "enter %d array values:\n",n );

	for ( i = 0; i < n ; i++ ){
		
		scanf( "%d", &input);
		
		ptr[i] = input;
	}

	for ( i = 0 ; i < n ; i++ )
		sum += ptr[i];
	
	printf( "the sum of the array elements is: %d\n" , sum);
	free(ptr);

	return 0;
}
