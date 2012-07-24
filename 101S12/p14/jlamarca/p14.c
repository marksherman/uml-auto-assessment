
/******************************************************/
/* Programmer: Joe LaMarca                            */
/* Program: p14 sine function                         */
/* Approximate time of completion: 25 min             */
/******************************************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main (int argc, char* argv[]){

  int x;

  x=atof(argv[1]);
  x=sin(x);
  printf("The sin of that is %d\n",x);

  return 0;
}
