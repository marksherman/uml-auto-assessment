/*********************************************************/
/*  Programmers: Zachary Durkee                          */
/*                                                       */
/*  Program 31: Inner Product of Two Vectors             */
/*                                                       */
/*  Approximate completion time: 1 hour                  */
/*********************************************************/

#include <stdio.h>

float inner( float u[], float v[], int size );

int main( int argc, char *argv[] ){

  float u[8], v[8], w;

  int i, j, size = 8;

  printf( "Enter an 8 value array for u:\n" );

  for( i=0; i<8; i++ ){

    scanf( "%f", &u[i] );

  }

  printf( "Enter an 8 value array for v:\n" );

  for( j=0; j<8; j++ ){

    scanf( "%f", &v[j] );

  }

  w = inner( u, v, size );

  printf( "The vector sum is:  %f\n", w );

  return 0;

}

float inner( float u[], float v[], int size ){

  float x, sum = 0;

  int i;

  for( i=0; i<size; i++ ){

    x = u[i] * v[i];

    sum = sum + x;
    
  }
  
  return sum;

}
