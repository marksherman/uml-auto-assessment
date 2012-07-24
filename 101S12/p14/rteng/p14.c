/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p14.c                          */
/*                         Due: 2/20/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h> 
int main(int argc, char* argv[])
{
  float x;
  x=atof(argv[1]);
  printf("The trigonometric sine value of the number represented by argv is:\n");
  x=sin(x);
  printf("%f\n", x);
  return 0;
}

