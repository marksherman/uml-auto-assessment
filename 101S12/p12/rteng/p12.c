/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p12.c                          */
/*                         Due: 2/19/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
#include <math.h>
int main(int argc, char* argv[])
{
  float x, y;
  printf("Input any positive floating point number:\n");
  scanf("%f", &x);
  y=sqrt(x);
  printf("The square root of the number is:\n");
  printf("%f\n", y);
  return 0;
}

