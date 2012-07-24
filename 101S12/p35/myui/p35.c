/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Passing a Two Dimensional Array  */
/*                                            */
/* Approximate completion time: 15 minutes    */
/**********************************************/

#include<stdio.h>

int add( int numbers[2][2] );

int main( int argc, char *argv[] ) {

  int sum, i, j, numbers[2][2];

  printf( "Enter 9 integers: " );
  
  for( i = 0; i < 3; i++ ) {
    for( j = 0; j < 3; j++ ) {
      scanf( "%d", &numbers[i][j] );
    }
  }

  printf( "The sum is %d.\n", sum );

  return 0;
}

int add( int numbers[2][2] ) {
  
  int sum, i, j;

  sum = 0;

  for( i = 0; i < 3; i++ ) {
    for( j = 0; j < 3; j++ ) {
      sum = sum + numbers[i][j];
    }
  }

  return sum;

}
