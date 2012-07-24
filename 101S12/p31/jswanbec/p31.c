/*******************************************************/
/* Programmer: Jimmy Swanbeck                          */
/*                                                     */
/* Program 31: Inner Product of Two Vectors            */
/*                                                     */
/* Approximate completion time: 20 minutes             */
/*******************************************************/

#include <stdio.h>

float inner( );

int main( int argc , char *argv[] )
{
  float p;
  float u[8];
  float v[8];
  printf("Input 8 values for the first vector: ");
  scanf("%f %f %f %f %f %f %f %f" , &u[0] , &u[1] , &u[2] , &u[3] , &u[4] , &u[5] , &u[6] , &u[7] );
  printf("Input 8 values for the second vector: ");
  scanf("%f %f %f %f %f %f %f %f" , &v[0] , &v[1] , &v[2] , &v[3] , &v[4] , &v[5] , &v[6] , &v[7] );
  p = inner( u , v , 8 );
  printf("Inner product of the two arrays is: %f\n" , p );
  return 0;
}

float inner( float u[] , float v[] , int size )
{
  float w = 0;
  int i;
  for( i = 0 ; i < size ; i++ )
    {
      w = w + ( u[i] * v[1] );
    }
  return w;
}
