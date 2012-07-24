/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Simulating Call By Reference     */
/*                                            */
/* Approximate completion time: 10 mins       */
/**********************************************/

#include<stdio.h>
#include<stdlib.h>

void swap( int *a, int *b );

int main( int argc, char *argv[] ) {

  int x, y;

  x = atoi( argv[1] );
  y = atoi( argv[2] );

  printf( "Input: x = %d, y = %d. \n", x, y );

  swap( &x, &y );

  printf( "Output: x = %d, y = %d. \n", x, y );

  return 0;
}

void swap( int *a, int *b ) {

  int temp;

  temp = *a;
  *a = *b;
  *b = temp;

  return ;

}
