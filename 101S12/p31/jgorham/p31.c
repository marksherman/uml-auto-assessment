/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 31 Vector Product                                                    */
/*                                                                              */
/* Approximate Completion Time:  15 min                                          */
/********************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

float inner(float u[] ,float v[] ,int size);

int main(int argc, char* argv[]){
  float u[8];
  float v[8];
  int i = 0;
  int size = 8;
  float product = 0;
  printf("Please populate array u:\n");
  for( i = 0 ; i < size ; i++)
    scanf("%f", &u[i]);
  printf("\nPlease populate array v:\n");
  for( i = 0 ; i < size ; i++)
    scanf("%f", &v[i]);
  product = inner(u , v , size);
  printf("\nThe inner product = %f\n", product);
  return 0;
}

float inner( float u[], float v[], int size){
  float temp[size];
  float product = 0;
  int i = 0;
  for( i = 0 ; i < size ; i++)
    temp[i] = u[i] * v[i];
  for( i = 0 ; i < size ; i++)
    product = temp[i] + product;
  return product;
}
