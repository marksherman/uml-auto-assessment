/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 35: Passing a Two Dimensional Array                            */
/*                                                                        */
/* Approximate Completion Time: 45 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int sum( int A[][3]);

int main( int argc, char *argv[] ) {

	int x [3][3];
	int y = 0, i , j ;

	printf( "\nPlease enter 9 integers: " );

	for ( i = 0; i < 3; i++ ){
		for ( j = 0; j < 3; j++){
			scanf("%d", &x[i][j]);
		}
	}

	y = sum( x );

	printf( "\nThe sum of all the values is %d.\n\n", y );

  return 0 ;

}

int sum( int A[][3] ){

	int i , j , sum = 0 ;

	for ( i = 0; i < 3; i++ ){
		for ( j = 0; j < 3; j++){
			sum = sum + A[i][j];
		}
	} 

	return sum;

}
