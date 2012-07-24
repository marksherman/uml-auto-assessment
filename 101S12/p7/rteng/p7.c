/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p7.c                           */
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
  if(x>0){
    printf("The number is positive.\n");
  }
  else if(x==0){
    printf("The number is zero.\n");
  }
  else{
    printf("The number is negative.\n");
  }
  return 0;
}

