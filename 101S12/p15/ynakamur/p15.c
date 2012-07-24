/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 15: Solid Box of Asterisks                           */
/*                                                              */
/* Approximate completion time: 20 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int length, height, i, j;

  printf( "Please enter a positive value for length that is less than 21.\n" );

  scanf( "%d", &length );

  printf( "Please enter a positive value for height that is less than 21.\n" );

  scanf( "%d", &height );

  for( i = 0; i < height; i++ ) {
    for( j = 0; j < length; j++ ) {
      printf( "%c", '*' );
    }
    printf( "\n" );
  }

  return 0;

}
