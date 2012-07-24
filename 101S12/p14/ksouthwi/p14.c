/********************************************/
/* Programmer: Kevin Southwick              */
/*                                          */
/* Program 14: Sine Function                */
/*                                          */
/* Approximate completion time: 10  minutes */
/********************************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main( int argc ,char* argv [] ) {

  float x ;

  x = atof (argv[1] );

  printf( " %e \n" , sin( x ) ) ;

  return 0;

}
