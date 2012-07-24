/******************************************************************/
/* Programmer: Yasutoshi Nakamura                                 */
/*                                                                */
/* Program 6: Equal to Zero?                                      */
/*                                                                */
/* Approximate completion time: 10 minutes                        */
/******************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int number;

  printf( "Please enter a single integer.\n" );

  scanf( "%d", &number );

  if( number == 0 ) {
    printf( "The number is equal to zero.\n" );
  }

  else {
    printf( "The number is not equal to zero.\n" );
  }

  return 0;

}
