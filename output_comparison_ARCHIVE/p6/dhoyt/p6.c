/*****************************/
/* Programmer: David Hoyt    */
/* Program: Equal to Zero?   */
/* Completion Time: 10min    */

#include <stdio.h>

int main(){

  int x;

  printf( "Enter a number:" );

  scanf( "%d", &x );

  if ( x == 0 )

    printf( "The number is equal to zero!\n" );

  else 

    printf( "The number is not equal to zero!\n" );

  return 0;
}
