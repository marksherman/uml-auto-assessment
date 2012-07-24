/*****************************************/
/* Programmer: Samantha M. Otten         */
/*                                       */
/*Program 31: Inner Product of 2 Vectors */
/*                                       */
/*Approx. Completion Time: 50 mins       */
/*                                       */
/*****************************************/
#include<stdio.h>

float inner(float u[], float v[], int size);

int main(int argc, char*argv[]){

  float s[8], m[8];

  scanf("%f%f",&s[8],&m[8]);

  printf("%f",inner(&s[8],&m[8],8));

  return 0;
}

float inner(float u[],float v[],int size){

  int ii;

  float o[8];

  for(i=0;i<size;i++){

    o[i]+=u[i]*v[i];

  }

  return o[i];
}
