
/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Programa: Sine Function                                               */
/*                                                                       */
/* Approximate completion time: 5 minutes                                */
/*************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main (int argc, char* argv[] ) {
  
  double f, Results;
  
  f = atof (argv[1]);
  
  Results = sin (f);
  
  printf("sin(%lf) = %lf\n",f,Results);
  
  return 0;
 
}
