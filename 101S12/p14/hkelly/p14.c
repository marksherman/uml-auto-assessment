/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 14: Sine Function                        */
/*                                                  */
/* Approximate completion time: 20 minutes          */
/****************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main( int argc, char* argv[] ){
  
  float x;
  float y;

  x = atof(argv[1]);
  y = sin( x );

  printf("\nThe sine of %f is %f\n",  x, y);

  return 0;
}
