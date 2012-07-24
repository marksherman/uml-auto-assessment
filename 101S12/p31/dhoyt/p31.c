/**********************************/
/* Programmer: David Hoyt         */
/* Program: Inner Product         */
/* Time: 30 min                   */

#include <stdio.h>
#include <stdlib.h>

float inner( float u[], float v[],  int size );

int main( int argc, char* argv[] ){

  float a[8]; 
  
  float b[8];

  int i;

  printf( "Enter eight(8) values for each array:\n" );

  printf( "Array 1:\n" );

  for( i=0; i<8; i++ ){

    scanf( "%f", &a[i] );

  }

  printf( "Array 2:\n" );

  for( i=0; i<8; i++ ){

    scanf( "%f", &b[i] );

  }

  printf( "Inner product is %f\n", (inner( a, b, 8 )));

  return 0;

}

float inner( float u[], float v[], int size ){

  int i; 

  float x=0;

  for( i=0; i<size; i++ ){

    x = x + ( (u[i])*(v[i]) );

  }

  return x;

}
