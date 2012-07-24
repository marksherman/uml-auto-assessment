/*****************************************/
/* Programmer: David Hoyt                */
/* Program: Positive, negative, or zero? */
/* Completion Time: 15min                */

#include <stdio.h>

int main(){

  int x;

  printf( "Enter a number:" );

  scanf( "%d", &x );

  if ( x > 0 )

    printf( "The number is positive!\n" );

  else if ( x < 0 )

    printf( "The number is negative!\n" );

  else

    printf( "The number is zero!\n" );

  return 0;
}
