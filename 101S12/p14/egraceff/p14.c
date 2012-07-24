/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Sine Function                                                    */
/*                                                                           */
/* Approximate completion time: 40 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int main( int argc, char *argv[])
{
  double x;
  x = atof(argv[1]);
  /* converts value from a string to a floating point */
  printf("The sine of %f is %f\n", x, sin(x));
  /* prints the sine of the value, x */
  return 0;
}
