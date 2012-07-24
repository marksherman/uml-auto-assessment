/***********************************************/
/* Programmer: Nathan Goss                     */
/*                                             */
/* Program 14: Sine Function                   */
/*                                             */
/* Approximate completion time: 3 minutes      */
/***********************************************/


#include <stdio.h>
#include <math.h>
#include <stdlib.h>


int main(int argc, char* argv[])
{
  float sine;

  sine = sin(atof(argv[1]));

  printf("%f\n", sine);

  return 0;
}
