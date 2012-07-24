/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p6.c                           */
/*                         Due: 2/15/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
int main(int argc, char* argv[])
{
  int x;
  printf("Input any integer here:\n");
  scanf("%d", &x);
  if(x==0){
    printf("The number is equal to zero.\n");
  }
  else{
    printf("The number is not equal to zero.\n");
  }
  return 0;
}

