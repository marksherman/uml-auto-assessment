/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 27: Reverse                                          */
/*                                                              */
/* Approximate completion time: 5  minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int numbers[10], i;

  printf( "\nPlease enter 10 integers.\n" );

  for( i = 0; i < 10; i++ ) {
    scanf( "%d", &numbers[i] );
  }

  for( i = 9; i >= 0; i-- ) {
    printf( "%d\n", numbers[i] );
  }

  return 0;

}

