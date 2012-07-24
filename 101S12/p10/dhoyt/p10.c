/**********************************/
/* Programmer: David Hoyt         */
/* Program: Sum of Twenty         */
/* Time: 20min                    */

#include <stdio.h>
#include <stdlib.h>

int main(){

  int x;

  int y;

  int z;

  z = 0;
  
  FILE* tst10;

  tst10 = fopen( "testdata10", "r" );

  for ( y = 0; y < 20; y++ ){

  fscanf( tst10, "%d", &x );

  z = z + x;

  }

  fclose( tst10 );

  printf( "%d\n", z );

  return 0;

}
