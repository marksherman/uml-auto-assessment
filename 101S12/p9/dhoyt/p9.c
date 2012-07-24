/**********************************/
/* Programmer: David Hoyt         */
/* Program: For Loop              */
/* Time: 15min                    */

#include <stdio.h>
#include <stdlib.h>

int main(){

  int x;
 
  int y;

  FILE* tst9;

  tst9 = fopen( "testdata9", "r" );

  for ( y = 0; y < 5; y++ ){

    fscanf( tst9, "%d", &x );

    printf( "%d\n", x );

  }

  fclose( tst9 );

  return 0;

}
