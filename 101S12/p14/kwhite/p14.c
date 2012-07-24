/************************************************/
/* Programmer: Kyle White                       */
/* Program  :                                   */
/* Approximate completion time:                 */
/*                                              */
/************************************************/


#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main (int argc, char* argv [])

{

  double x,y;

  x = atof (argv [1]);

  y = sinh (x);

  printf("\n%f\n\n", y); 

  return 0;

}
