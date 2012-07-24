/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program:p42 Malloc 2D           */
/*                                 */
/* Approx Completion Time: 3 hours */
/***********************************/

#include<stdio.h>
#include<stdlib.h>

int main( int argc, char *argv[]){

	int* a;                     /* a is a pointer to an integer*/
	int r,c,i,j;                /* r and c is for rows and columns*/
	int sum = 0;

	printf( "Please enter values for r and c: " );

	scanf( "%d %d",&r, &c ); /* store values into r an c locations */
	
	a = (int *)malloc(r*c*sizeof(int));  /* single call to malloc */

	printf( "Please enter values to fill the array:" );
       
	
	for( i = 0; i < r; i++){
		for( j = 0; j < c; j++){
			scanf( "%d", &a[i*c+j] );
		}
	}
	
	sum = 0;
	printf( "Which row would you like summed?:" ); 
	scanf( "%d", &j );
	for( i = 0; i < c; i++){
		sum+=a[j * c + i];
	}
	printf( "The sum of the row is:%d\n", sum );
	sum = 0;
	printf( "Which column would you like summed?:" );
	scanf( "%d", &j );
	for ( i = 0; i < r ; i++ ){
		sum+=a[i * r + j];
	}
	printf( "The sum of the columns is:%d\n", sum );
		
	sum = 0;       
	for( i = 0; i < r * c; i++){
		
		sum+=a[i];
	}
	printf( "The sum of the array is:%d\n" , sum );
	
	free(a);
	return 0;
}





	
	
	
