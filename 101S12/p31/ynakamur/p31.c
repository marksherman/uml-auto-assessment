/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 31: Inner Product of Two Vectors                                  */
/*                                                                           */
/* Approximate completion time: 30 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

float inner( float u[], float v[], int size );

int main( int argc, char *argv[] ) {

  int size = 8, i, j;
  float answer, vector1[size], vector2[size];

  printf( "\nPlease input 8 floating point values.\n" );

  for( i = 0; i < size; i++ ) {
    scanf( "%f", &vector1[i] );
  }

  printf( "Please input 8 more floating point values.\n" );

  for( j = 0; j < size; j++ ) {
    scanf( "%f", &vector2[j] );
  }

  answer = inner( vector1, vector2, size );

  printf( "The inner product is equal to %f\n\n", answer );

  return 0;

}


float inner( float u[], float v[], int size ) {

  int i;
  float storage[size], answer;

  for( i = 0; i < size; i++ ) {
    storage[i] = u[i] * v[i]; 
  }

  answer = storage[0];

  for( i = 1; i < size; i++ ) {
    answer = answer + storage[i];
  }

  return answer;

}
