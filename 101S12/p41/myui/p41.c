/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Malloc up a space for 1d array   */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>
#include<stdlib.h>

int main( int argc, char *argv[] ) {


  int n, i, sum;
  int *num;

  n = 0;
  sum = 0;
  
  printf( "Enter the number of integers: " );
  scanf( "%d", &n );

  num = malloc( n * sizeof( int ) );

  for( i = 0; i < n; i++ ) {
    printf( "Enter the %dth number: ", i + 1 );
    scanf( "%d", &num[i] );
  }

  for( i = 0; i < n; i++ ) {
    sum = sum + num[i];
  }

  printf( "The sum of these %d terms is %d.\n", n, sum );

  free( num );

  return 0;
}
