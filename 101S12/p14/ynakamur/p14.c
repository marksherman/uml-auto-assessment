/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 14: Sine Function                                    */
/*                                                              */
/* Approximate completion time: 10 minutes                      */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main( int argc, char *argv[] ) {

  float value;

  value = atof( argv[1] );

  value = sin( value );

  printf( "%f\n", value );

  return 0;

}
