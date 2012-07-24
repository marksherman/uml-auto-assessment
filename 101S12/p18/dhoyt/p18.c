/************************************/
/* Programmer: David Hoyt           */
/* Program: Area of a Circle        */
/* Time: 15min                      */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main(){

  double r;

  printf( "Enter the radius of your circle:" );

  scanf( "%lf", &r );

  printf( "Are of your circle: %f\n", r*r*M_PI );

  return 0;

}
