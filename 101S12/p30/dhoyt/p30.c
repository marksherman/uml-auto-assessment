/****************************************/
/* Program: David Hoyt                  */
/* Program: Call By Rference            */
/* Time: 15min                          */

#include <stdio.h>
#include <stdlib.h>

void swap( int *a, int *b );

int main( int argc, char* argv[] ){

  int x,y;

  printf( "Enter two values\n" );

  scanf( "%d%d", &x, &y );

  printf( "x is %d and y is %d\n", x, y );

  swap( &x, &y );

  printf( "New x: %d, New y: %d\n", x, y );

  return 0;

}

  void swap( int *a, int *b ){

    int tmp;

    tmp = *a;

    *a = *b;

    *b = tmp;

    return;

  }
