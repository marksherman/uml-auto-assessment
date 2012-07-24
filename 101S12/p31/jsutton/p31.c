/********************************************/
/* Programmer: Joanna Sutton                */
/*                                          */
/* Assignment: Inner Product of Two Vectors */
/*                                          */
/* Approximate Completion Time:20 minutes   */
/********************************************/

#include <stdio.h>

float inner (float u[], float v[], int size);

int main (int argc, char *argv[]){
  float vector1[8];
  float vector2[8];
  int i;
  float s;

  printf("Please input 8 numbers separated by spaces.\n");
  
  for(i=0;i<8;i++)
    scanf("%f", &vector1[i]);

  printf("Please input 8 numbers separated by spaces.\n");
  
  for (i=0;i<8;i++)
    scanf("%f", &vector2[i]);

  s=inner(vector1,vector2,8);

  printf("The inner product of these vectors is %f\n", s);

  return 0;

}

float inner (float u[], float v[], int size){
  int i;
  float x;
  float s=0;
  for (i=0;i<size;i++){
    x=u[i]*v[i];
    s=x+s;
  }

  return s;

}
