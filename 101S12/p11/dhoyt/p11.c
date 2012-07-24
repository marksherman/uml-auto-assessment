/******************************/
/* Programmer: David Hoyt     */
/* Program: abs               */
/* Time: 30min                */

#include <stdio.h>
#include <stdlib.h>

int main(){

  int x;

  printf( "Enter a number:" );

  scanf( "%d", &x );

  printf( "The absolute value is: %d", abs( x ) );      

 putchar( '\n' );

 return 0;

}
