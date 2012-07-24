/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/* Program 8: 1 horizontal line * */
/*                                */
/*Approx. Completion Time: 15mins */
/*                                */
/**********************************/

#include <stdio.h>
#include <stdlib.h>

int main(){

  int a;

  int b;

  FILE* testdata8;

  testdata8 = fopen( "testdata8", "r" );

  fscanf( testdata8, "%d", &a );

  fclose( testdata8 );

  for( b=0; b<a; b++ ){

    printf( "*" );

  }

  putchar( '\n' );

  return 0;

}
