/***********************************/
/* Programmer: David Hoyt          */
/* Program: argv/sin               */
/* Time: 15min                     */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main( int argc, char* argv[] ){

  float x;

  x = atoi( argv[1] );

  printf( "The sine value of the argument is: %f\n", sin( x ));

  return 0;

}
