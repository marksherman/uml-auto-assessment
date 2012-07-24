/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Sine Function                    */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>
#include<math.h>
#include<stdlib.h>

int main( int argc, char *argv[] ) {

  float x = atof( argv[1] );
  const double PI = 3.14159265359;

  printf( "%f\n", sin( x * PI / 180 ) );

  return 0;
}
