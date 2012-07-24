/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 14: Sine Function                                      */
/* Approx Completion Time: 15 Mintues                             */
/******************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main( int argc, char* argv [] ){
   
  float x;

  x = atof(argv[1]);
  x = sin(x);
  printf( "The sine of the value you entered is %f\n", x );    

  return 0;
}

