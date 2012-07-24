/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p11.c                          */
/*                         Due: 2/19/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[])
{
  int x, y;
  printf("Input any integer:\n");
  scanf("%d", &x);
  y=abs(x);
  printf("The absolute value of the integer is:\n");
  printf("%d\n", y);
return 0;
}
