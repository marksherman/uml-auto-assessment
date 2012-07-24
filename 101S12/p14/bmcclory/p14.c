/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #14: Sine Function                            */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main(int argc, char* argv[])

{

  float i;

  i = atof(argv[1]);

  i = sin(i);
  
  printf("%f\n", i);
  
  return 0;
}
