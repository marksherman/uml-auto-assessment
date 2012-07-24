/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Using the sqrt Function                                          */
/*                                                                           */
/* Approximate completion time: 10 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <math.h>
int main( int argc, char *argv[] )
{
  float x;
  printf("Please enter a floating point number: \n");
  /* prompt the user for a floating point number */
  scanf("%f", &x);
  /* store the value in the variable x */
  printf("The square root of the value is: %f\n", sqrt(x));
  /* print square root of the value */
  return 0 ;
}
