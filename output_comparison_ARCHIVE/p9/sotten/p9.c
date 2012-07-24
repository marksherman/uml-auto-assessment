/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/* Program 9: Using a for loop    */
/*                                */
/*Approx. Completion Time: 20mins */
/*                                */
/**********************************/


#include <stdio.h>
#include <stdlib.h>

int main(){

  int a;

  int b;

  FILE* testdata9;

  testdata9 = fopen( "testdata9", "r" );

  for ( b = 0; b < 5; b++ ){

    fscanf( testdata9, "%d", &a );

    printf( "%d\n", a );

  }

  fclose( testdata9 );

  return 0;

}
