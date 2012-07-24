/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 25: Unfilled Box                                     */
/*                                                              */
/* Approximate completion time: 25 minutes                      */
/****************************************************************/

#include <stdio.h>

int function( int length, int height );
int function2( int length, int height );

int main( int argc, char *argv[] ) {

  int length, height;

  printf( "\nEnter values for length and height respectively.\n" );

  scanf( "%d %d", &length, &height );

  if( length <= 2 || height <= 2 ) {
    function( length, height );
  }

  if( length >= 3 && height >=3 ) {
    function2( length, height );
  }

  return 0;

}
 

int function( int length, int height ) {

  int i, j;

  for( i = 0; i < height; i++ ) {
    for( j = 0; j < length; j++ ) {
      printf( "%c", '*' );
    }
    printf( "\n" );
  }

  return 0;

}


int function2( int length, int height ) {

  int i, j, k, l;

  for( i = 0; i < length; i++ ) {
    printf( "%c", '*' );
  }
      
  printf( "\n*" );

  for( j = 0; j < height - 2; j++ ) {
    for( k = 0; k < length - 2; k++ ) {
      printf( " " );
    }
    printf( "*\n*" );
  }
    
  for( l = 0; l < length - 1; l++ ) {
    printf( "%c", '*' );
  }

  printf( "\n" );

  return 0;

}
