/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 14:   The Sine Function                      */
/* Time:         60 minutes(forgot gcc -lm for 50min)   */
/********************************************************/
#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main ( int argc, char *argv[] ) { 
  float result;
  float s=atoi(argv[1]);
  result=sin(s);
printf("The the sine of %s is %f\n",argv[1],result);
  return 0;
} 
