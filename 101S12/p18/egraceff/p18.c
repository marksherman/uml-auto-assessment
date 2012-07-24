/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Area of a Circle                                                 */
/*                                                                           */
/* Approximate completion time: 15 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <math.h>
int main( int argc, char *argv[] )
{
  float r;
  float A;
  printf("Please enter the radius of the circle you wish to know the area of:\n");
  /* prompts the user for the radius of the circle */
  scanf("%f", &r);
  /* stores the value of the radius into the variable, r */
  A = M_PI * r * r;
  /* calculates the area of the circle */
  printf("The area of the circle of radius %f is equal to %f.\n", r, A);
  /* prints the area, A, of the circle */
  return 0 ;
}
