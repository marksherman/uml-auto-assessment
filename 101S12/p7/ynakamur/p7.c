/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 7: Positive, Negative, or Zero?                      */
/*                                                              */
/* Approximate completion time: 10 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int number;

  printf( "Please enter a single integer.\n" );

  scanf( "%d", &number );

  if( number == 0 ) {
    printf( "The number is zero.\n" );
  }

  if( number > 0 ) {
    printf( "The number is positive.\n" );
  }

  if( number < 0 ) {
    printf( "The number is negative.\n" );
  }

  return 0;

}
