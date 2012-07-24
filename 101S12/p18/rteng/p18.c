/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p18.c                          */
/*                         Due: 2/29/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/
#include <stdio.h>
#include <math.h>
int main(int argc, char* argv[])
{
  float r;
  /*defining radius as a float type*/
  printf("Input the radius of desired circle: ");
  /*ask for user input of radius*/
  scanf("%f", &r);
  /*assign radius to variable r*/
  printf("The radius given for the circle is: %f units.\n", r);
  /*state input value of radius*/
  printf("The area of the circle is: %f units squared.\n", M_PI*r*r);
  /*calculates and prints area of circle with given radius*/
  return 0;
}
