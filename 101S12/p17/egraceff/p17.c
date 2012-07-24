/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Area of a Rectangle                                              */
/*                                                                           */
/* Approximate completion time: 15 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  float l;
  float h;
  float A;
  printf("Please enter the length and height respectively of the rectangle you wish to know the area of:\n");
  /* prompts the user for the dimensions of the rectangle */
  scanf("%f %f", &l, &h);
  /* stores the values of the length and height of the rectangle in the variables l and h respectively */
  A = l * h;
  /* calculates the area of the rectangle, A, by multiplying the length, l, by the height, h */
  printf("The area of the rectangle of length %f and height %f is equal to %f.\n", l, h, A);
  /* prints the area, A, of the rectangle */
  return 0;
}
