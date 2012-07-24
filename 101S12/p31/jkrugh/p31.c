/*************************************************************/
/* Programmer: Jeremy Krugh                                  */
/* Program 31: Inner Product of Two Vectors                  */
/* Approximate completion time: 45 minutes                   */
/*************************************************************/

#include <stdio.h>

float inner(float u[], float v[], int size);

int main(int argc, char* argcv[]){

  float j[8];
  float k[8];

  j[8]=atof(argv[8]);
  k[8]=atof(argv916]);

inner(j[8],k[8],16);

printf("%f", sum);

return 0;
}

float inner(float u[], float v[], int size){

  float w;
  float sum;
  float prod;

  w = size;
  for(size = 0; size<=w; size++){
    prod = u[size] * v[size];
    sum += prod;
  }

  return sum;
}
