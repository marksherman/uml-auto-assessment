/********************************************/
/* Programmer: Rachel Driscoll              */
/*                                          */
/* Program: Passing a Two Dimensional Array */
/*                                          */
/* Approx Completion Time: 1 hour           */
/********************************************/


#include<stdio.h>

#define row 3
#define column 3


int matrix_sum(int m[][3]);

int main( int argc, char *argv[]){
  
	int m[3][3];
	int i, j;

	printf( " Enter 9 digits here:");
	
	for( i = 0; i  < 3; i++){
		for( j = 0; j < 3; j++){
			scanf( "%d", &m[i][j] );
		}
	}			
	printf( "The sum of the digits is:%d\n", matrix_sum(m)); 
	
	return 0;
}

int matrix_sum(int m[][3]){

	int i,j;
	int sum = 0;

	for( i = 0; i < 3; i++){
		for(j = 0; j < 3; j++){
			sum += m[i][j];
		}
	}
	return sum;
}

		/*(*m)+(2*1+2)*/

/*   (m[1][2]) is the same as m+2*1+2    */
/*   m[i+(j*width)] = whatever           */
/*    matrix(m,9);               */ 
