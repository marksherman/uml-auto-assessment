/****************************************************************************/
/* Jennifer Ly                                                              */
/* p12.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include <stdio.h>
#include <math.h>
int main(int argc, char* argv[])
{
  float x, y;
  printf("Input a positive floating point number:\n");
  scanf("%f", &x);
  y=sqrt(x);
  printf("The square root of it is:\n");
  printf("%f\n", y);
  return 0;
} 
 
