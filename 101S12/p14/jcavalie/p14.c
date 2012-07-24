/***************************/
/* John Cavalieri        */
/* p14 sine function    */
/* 10 minutes           */   
/************************/


#include<stdio.h>
#include<math.h>
#include<stdlib.h>

int main(int argc, char* argv[]){

  double x;
  double sine;

  x = atof(argv[1]);

  sine = sin(x);

  printf("the sine of the angle is: %f (rads)\n", sine);

  return 0;
}
