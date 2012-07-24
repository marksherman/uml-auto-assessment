/********************************************/
/* Programmer: Joanna Sutton                */
/*                                          */
/* Assignment: Sine Function                */
/*                                          */
/* Approximate Completion Time: 10 minutes  */
/********************************************/
#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main(int argc, char* argv[]){
  float x;
  x=atof (argv[1]);
  x=sin(x);
  printf("sin(x)=%f\n",x);

  return 0;
}
