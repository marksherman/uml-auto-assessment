/*****************************/
/* Programmer: David Hoyt    */
/* Program: 1-D Array        */
/* Time: 10 min              */

#include <stdio.h>
#include <stdlib.h> 

int main(){

  int x[15], i;

  FILE* test26;

  test26 = fopen( "testdata26", "r" );

  for( i=0; i<15; i++ ){

    fscanf( test26, "%d", &x[i] );

  }

  fclose( test26 );

  for( i=14; i>-1; i-- ){

    printf( "%d\n", x[i] );

  }

  return 0;

}
