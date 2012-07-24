/************************************************/
/* Programmer: Kyle White                       */
/* Program  18: Area of a Circle                */
/* Approximate completion time: 20 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>
#include <math.h>

int main (int argc, char* argv [])

{

  float radius;
  float area;
  float pi;

  pi = M_PI;

  printf ("\nPlease Enter a Radius:");

  scanf ("%f", &radius);

  area = pi * radius * radius;

  printf ("Area of a circle with radius %f = %f\n\n", radius, area);

  return 0;

}
