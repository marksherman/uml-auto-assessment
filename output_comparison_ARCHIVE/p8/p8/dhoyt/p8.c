/******************************************/
/* Programmer: David Hoyt                 */
/* Program: Horizontal  Asterisks         */
/* Time: 20min                            */

#include <stdio.h>
#include <stdlib.h>

int main(){

  int x;

  int y;
  
  FILE* tst8;
 
  tst8 = fopen( "testdata8", "r" );

  fscanf( tst8, "%d", &x );

  fclose( tst8 );

  for( y=0; y<x; y++ ){

    printf( "*" );

  }

  putchar( '\n' );

  return 0;

}
