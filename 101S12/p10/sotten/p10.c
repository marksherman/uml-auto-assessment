/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 10: Sum of Twenty       */
/*                                */
/*Approx. Completion Time: 15mins */
/*                                */
/**********************************/

#include <stdio.h>
#include <stdlib.h>

int main(){

  int a;

  int b;

  int c;

  c = 0;

  FILE* testdata10;

  testdata10 = fopen( "testdata10", "r" );

  for ( b = 0; b < 20; b++ ){

    fscanf( testdata10, "%d", &a );

    c = c + a;

  }

  fclose( testdata10 );

  printf( "%d\n", c );

  return 0;

}


