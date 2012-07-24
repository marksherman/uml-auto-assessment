/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 31: Inner Product of Two Vectors      */
/* Approximate completion time: 3:30 hours       */
/*************************************************/

#include <stdio.h>
#include <stdlib.h>

float inner( float u[ ], float v[ ], int size );

int main( int argc, char* argv[] ) {
  
  float u[8], v[8];
  int size, i;
 /* 
  printf( "\nEnter the number of elements the vectors will contain: " );
  
  scanf( "%d", &size ); */
  size = 8;
  
  if( (size >= 0) && (size <= 8) ){
    
    printf( "\nEnter %d floating point value(s) for each vector to obtain their dot product:\n\nVector 1: ", size );
    
    for( i = 0; i < size; i++ )
      scanf( "%f", &u[i] );  
    
    printf( "\nVector 2: " );
    
    for( i = 0; i < size; i++ )
      scanf( "%f", &v[i] );
    
    printf( "\nThe dot product of the two vectors is: %f\n\n", inner( u, v, size ) );  
  }
  else
    printf( "\nThe number of elements each vector can contain must be from 0 to 8 inclusively.\n\n" );
  
  return 0;
}

float inner( float u[ ], float v[ ], int size )
{
  int i;
  float q[8], sum = 0;
  
  for( i = 0; i < size; i++ ){
    q[i] = u[i] * v[i];
    sum = sum + q[i];
  }
  return sum;
}
