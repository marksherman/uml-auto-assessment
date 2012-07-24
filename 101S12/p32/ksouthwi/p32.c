/********************************************/
/* Programmer: Kevin Southwick              */
/*                                          */
/* Program 32: Non-recursive Factorial      */
/*                                          */
/* Approximate completion time: 15  minutes */
/********************************************/

#include <stdio.h>

int factorial(  int n );

int main( int argc , char* argv [] ) {

  int x;

  printf( "input an integer greater than or equal to 0. \n" );

  scanf( "%d" , &x );

  printf( "%d factorial is: %d \n" , x , factorial( x ) );

  return 0;

}

int factorial( int n ){

  int j = 1;

  if( n == 0 ) /*the case that won't work in the loop*/

    return 1;

  else {

    for( n = n ; n >= 1 ; n-- ) /*permutates all other factorials*/

      j = j * n;

    return j;

  }

}
