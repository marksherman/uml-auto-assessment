/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 31: Inner Product of Two Vectors         */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

float inner( float u[], float v[], int size );
int main( int argc, char* argv[] ){

  float x[8];
  float y[8];
  int i;
  float pro = 0;
  int size = 8;
  
  printf("\nEnter 8 values to store in the first array:\n");
  for(i = 0; i < 8; i++){
    scanf("%f", &x[i]);
  }

  printf("\nEnter 8 values to store in the second array:\n");
  for(i = 0; i < 8; i++){
    scanf("%f", &y[i]);
  }

  pro = inner( x, y, size );

  printf("\nThe inner product of the two vectors is: %f\n", pro);

  return 0;

}

float inner( float u[], float v[], int size ){

  int i;
  float pro = 0;
  float temp = 0;
  
  for( i = 0; i < size; i++ ){
  temp = u[i] * v[i];
  pro  = pro + temp;
}

  return pro;
}
