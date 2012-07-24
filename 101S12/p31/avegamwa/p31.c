/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p31: Product of 2 Vectors      */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

float inner( float u[] , float v[], int size );

int main(int argc, char* argv[])
{

  float u[8], v[8]; 
 
  int i, j ;
  float ans ;

 
  for( i = 0 ; i < 8 ; i++) {
    printf("Print first 8 values for the first vector:");
    scanf("%f", &u[i]);
  }

  for( j = 0 ; j < 8 ; j++ ) {
    printf("Print next 8 values for the second vector:");
    scanf("%f", &v[j]);
  }

  ans = inner( u, v, 8 );

  printf("The answer is: %f", ans);
  printf( "\n");

  return 0;
}

float inner( float u[], float v[], int size ){

  float sum = 0;
  int i;
  
  for( i=0; i<size; i++ ){
    sum = sum + (u[i] * v[i]);
  }

  return sum ;
}
