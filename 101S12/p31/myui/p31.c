/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Inner Product of Two Vectors     */
/*                                            */
/* Approximate completion time: 15 minutes    */
/**********************************************/

#include<stdio.h>

float inner( float u[], float v[], int size );

int main( int argc, char *argv[] ) {

  float ans, i, vec1[8], vec2[8];
  int size;

  size = 8;

  printf( "Enter the numbers of the first vector:" );
  for( i = 0; i < size; i++ )
    scanf( "%f", &vec1[i] );
  
  printf( "Enter the numbers of the second vector:" );
  for( i = 0; i < size; i++ )
    scanf( "%f", &vec2[i] );

  ans = inner( vec1, vec2, size );

  printf( "The inner product is %f.\n", ans );

  return 0;
}

float inner( float u[], float v[], int size ) {

  float temp[8], ans;
  int i;

  ans = 0;

  for( i = 0; i < size; i++ ) 
    temp[i] = u[i] * v[i];
  
  for( i = 0; i < size; i++ )
    ans = ans + temp[i];

  return ans;

}
