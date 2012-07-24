/*************************************************/
/* Programmer: Nathan Goss                       */
/*                                               */
/* Program 18: Area of a Rectangle               */
/*                                               */
/* Approximate completion time: 5 minutes        */
/*************************************************/


#include <stdio.h>
#include <math.h>

int main(int argc, char* argv[])
{
  float radius;

  printf("Input float value of radius: ");
  scanf("%f", &radius);

  printf("A circle with radius %f has an area of %f\n",
	 radius, radius * radius * M_PI);

  return 0;
}
