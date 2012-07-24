/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 14: Sine Function                 */
/*                                           */
/* Approximate completion time: 22 minutes   */
/*********************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main(int argc,char* argv[])
{
  float x;
  x=atof(argv[1]);
  printf("The sine of %f is: %f\n",x,sin(x));
  return 0;
}
