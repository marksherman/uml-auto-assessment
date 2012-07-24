/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #31: Inner Product of Two Vectors             */
/*                                                       */
/* Approximate Completion Time: 30 Minutes               */
/*********************************************************/

#include <stdio.h>

float inner(float x[], float y[], int size);

int main(int argc, char* argv[]){

  float x[8], y[8];

  float product;

  int i, j;

  int size = 7;

  printf("Type eight floating point numbers: ");

  for(i = 0; i < 8; i++){
    scanf("%f", &x[i]);
  }

  printf("Type eight floating point numbers: ");

  for(j = 0; j < 8; j++){
    scanf("%f", &y[j]);
  }

  inner(float x[], float y[], int size);

  printf("%f", product);

  return 0;

}

float inner(float x[], float y[], int size){

  float product;
  
  int i;
  
  for(i = 0; i < size; i++){
    product = x[i] * y[i];
  }

  return product;

}
