/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 33: Recursive Factorial                                           */
/*                                                                           */
/* Approximate completion time: 10 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int factorial( int n );

int main( int argc, char *argv[] ) {

  int number, result;

  printf( "\nPlease input an integer.\n" );

  scanf( "%d", &number );

  result = factorial( number );

  printf( "The factorial of the integer is equal to %d.\n\n", result );

  return 0;

}


int factorial( int n ) {

  if( n == 1 || n == 0 ) {
    return 1;
  }

  else {
    return n * factorial( n - 1 );
  }
}

