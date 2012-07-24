/*******************************/
/* Programmer: David Hoyt      */
/* Program: Passing 2-D Array  */
/* Time: 30 min                */

#include <stdio.h>
#include <stdlib.h>

int sum( int rows, int columns, int values[rows][columns] );

int main(){

  int i, j;

  int values[3][3];

  printf( "Enter 9 numbers:" );

  for( i=0; i<3; i++ ){
  
    for( j=0; j<3; j++ )

      scanf( "%d", &values[i][j] );
  
  }

  printf( "%d\n", sum( i, j, values ));
   
  return 0;  

}

int sum( int rows, int columns, int values[rows][columns] ){

  int sum=0, i, j;

    for( i=0; i<3; i++ ){

      for( j=0; j<3; j++ )

	sum = sum + values[i][j];

    }

    return sum;

}
