/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 31: Inner Product of Two vector */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

float inner( float u[] , float v[], int size );
int main(int argc, char* argv[])
{
  
  float u[8], v[8];
  int i, j ;
  float x ;
  
  printf("\nplease enter 8 integers\n");
  for( i = 0 ; i < 8 ; i++) {
    scanf("%f", &u[i]);
  }
  printf("\nplease enter another 8 integers\n");
  for( j = 0 ; j < 8 ; j++ ) {
    scanf("%f", &v[j]);
  }
  x = inner( u, v, 8 );
  printf("The results are: %f", x);
  printf( "\n");
  
  return 0;

}
float inner( float u[], float v[], int size ){

  float z = 0;
  int i;
  
  for( i=0; i<size; i++ ){
    z = z + (u[i] * v[i]);
  }
  
  return z ;
}
