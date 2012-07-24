/****************************/
/* Programmer: David Hoyt   */
/* Program: sqrt            */
/* Time: 10min              */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main(){

  float x;

  printf( "Enter a number:" );

  scanf( "%f", &x );

  printf( "The square root is: %f", sqrt( x ) );

  putchar ( '\n' );

  return 0;

}
