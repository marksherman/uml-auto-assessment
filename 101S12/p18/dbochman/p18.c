/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 18:   Area of a Circle                       */
/* Time:         5 minutes                              */
/********************************************************/
#include <math.h>
#include <stdio.h>

int main ( int argc, char *argv[] ) {

  float r,a;

  printf("Please enter a lenth for the radius of a circle\n");

  scanf("%f",&r);

  a=M_PI*r*r;

  printf("The area of the circle is %f\n",a);

  return 0;
}
