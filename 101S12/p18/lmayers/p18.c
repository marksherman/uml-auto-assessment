/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Area of a circle                                             */
/*                                                                       */
/* Approximate completion time: 10 minutes                               */
/*************************************************************************/
#include <stdio.h> 
#include <math.h>

int main ( int argc, char *argv[] ) {

  float Radius, area;

  printf("Please enter a single floating point number for the radius:\n");

  scanf("%f", &Radius); 
 
  area =  (Radius * Radius * M_PI);

  printf("The area of the circle is : %f\n", area);

  return 0;
}
