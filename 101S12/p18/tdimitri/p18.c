/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 18: Area of a Circle                  */
/* Approximate completion time: 20 mins          */
/*************************************************/
#include <stdlib.h>
#include <math.h>
#include <stdio.h>
int main(int argc, char* argv[]) {
  float r;
  printf( "Enter a value to find the area of a circle with radius equal to that value: \n");
  scanf("%f", &r);
  printf("The area of that circle is: %f\n", M_PI*r*r);
  return 0;
}
