/********************************************/
/* Programmer: Joanna Sutton                */
/*                                          */
/* Assignment: Find the Average             */
/*                                          */
/* Approximate Completion Time: 10 minutes  */
/********************************************/

#include <stdio.h>

int main (int argc, char*argv[]){
  float integers[4];
  FILE *numbers;
  float x=0;
  int y;
  numbers=fopen("testdata24","r");

  for(y=0;y<5;y++){
  fscanf(numbers,"%f", &integers[y]);
  }

  x=integers[0]+integers[1]+integers[2]+integers[3];
  x=x/4;
  printf("The average is %f.\n", x);

  fclose(numbers);

  return 0;

}
