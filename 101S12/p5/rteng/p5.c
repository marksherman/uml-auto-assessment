/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p5.c                           */
/*                         Due: 2/13/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
int main()
{
  int x;
  printf("Input any integer here:\n");
  scanf("%d", &x);
  if(x>100){
    printf("The number is bigger than 100\n");
  }
  else{
    printf("The number is not bigger than 100\n");
  }
  return 0;
}
