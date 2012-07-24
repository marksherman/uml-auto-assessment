/********************************/
/* Author: James DeFilippo      */
/* Title: Sine Function         */
/* Approximate Time: 10 minutes */
/********************************/

#include<stdio.h>
#include<math.h> /* sin() function is stored here */ 
#include<stdlib.h> /* atof is stored here */ 
int main ( int argc, char *argv[] )
{
  double x; /* initialize some variable to store whatever exists in argv[1] */ 
  x = atof ( argv[1] );  /* converts the string stored in the array to a variable x of type double */ 
  x = sin( x ); /* invoke a function from math.h, make x an argument, and store the result as the new value of x */ 
  printf("%lf\n", x ); /* display result of mathematical operation */ 
  return 0;
}

