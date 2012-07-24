/************************************************/
/* Programmer: Kyle White                       */
/* Program  31: Inner Product of Two vectors    */
/* Approximate completion time: 20 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>


int size;
float inner( float u[size], float v[size], int size );
int main (int argc, char* argv [])

{

  int totsum=0;
  int x=0,y=0,a=0,b=0;
  float vec1[8];
  float vec2[8];

  printf("\nEnter two vectors, each with 8 elements:\nvector 1: ");

  for ( a=0; a<8; a++ ){

    scanf( "%d", &x );

    vec1[a] = x;

  }

  printf("Vector 2: ");

  for ( b=0; b<8; b++ ){

    scanf( "%d", &y );

    vec2[b] = y;

  }

  totsum = inner (vec1, vec2,8);

  printf("\nThe sum of the inner product of the two vectors is: %d\n\n", totsum);

  return 0;

}

float inner( float u[size], float v[size], int size )

{

  int i=0,x=0;
  float sum=0;
  
  for ( i=0; i<size; i++ ){

    x = u[i]*v[i];

    sum = sum + x;

  }

  return sum;

}
