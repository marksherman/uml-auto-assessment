/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p27: Reverse                                  */
/*                                                       */
/* Approximate completion time: 20 minutes               */
/*********************************************************/
#include <stdio.h>
int main(int arc, char* argv[])
{
  int p27[10], i;
  /*declares p27 as an array of 10 memory locations*/
  printf("Please enter 10 integers and hit 'Enter': ");
  for(i = 0; i < 10; i++)
    {
      /*Will perform loop 10 times*/
      scanf("%d", &p27[i]);
      /*Stores each integer into the array at the corresponding memory location of i*/
    }
  printf("The numbers entered will be shown in reverse order: \n");
  for(i = 9; i >= 0; i--)
    {
      printf("%d ", p27[i]);
      /*Prints out the integers in the array in reverse order, starting from p27[9]*/
    }
  printf("\n");
  return 0;
}
