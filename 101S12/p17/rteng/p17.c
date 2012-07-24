/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p17.c                          */
/*                         Due: 2/28/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/
#include <stdio.h>
int main(int argc, char* argv[])
{
  double l, h;
  printf("Input length followed by height of desired rectangle: \n");
  scanf("%lf %lf", &l, &h);
  printf("The area of the rectangle is: %lf units squared\n", l*h);
  return 0;
}
