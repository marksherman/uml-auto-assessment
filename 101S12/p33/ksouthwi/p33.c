/********************************************/
/* Programmer: Kevin Southwick              */
/*                                          */
/* Program 33: Recursive Factorial          */
/*                                          */
/* Approximate completion time: 10  minutes */
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

  if( ( n == 0 ) || ( n == 1 ) ) /*base cases*/

    return 1;

  else {

    n = ( n * factorial( n - 1 ) ); /*recursive call*/

    return n;

  }

}
