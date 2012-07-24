/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 15: Passing a Two Dimensional Array      */
/*                                                  */
/* Approximate completion time: 30 minutes          */
/****************************************************/

#include <stdio.h>

int sum( int array[][3], int rows, int columns );
int main( int argc, char* argv[] ){

  int array[3][3];
  int x = 0;
  int y = 0;
  int rows = 3;
  int columns = 3;
  int total = 0;

  for( x = 0; x < 3; x++ ){ /* Iterates x (#rows) and y (#columns) so 
			       that the ints entered are stored in the 
			       imaginary table */

    for( y = 0; y < 3; y++ ){
      printf("\nEnter integers to fill up a 3x3 array\n");
      scanf("%d", &array[x][y]);
    }

  }
   
  total = sum( array, rows, columns ); /* Calls the sum function */

  printf("\nThe sum of this array is: %d\n", total); /* Prints total */

  return 0;
}

int sum( int array[][3], int rows, int columns ){

  int x;
  int y;
  int total = 0;

  for( x = 0; x < rows; x++ ){    /* Starts adding together all the ints */
    for( y = 0; y < columns; y++){
      total += array[x][y];
  }
   }
  
  return total;
}

