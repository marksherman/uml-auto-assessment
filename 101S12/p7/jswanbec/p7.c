/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 6: Equal to Zero?                 */
/*                                           */
/* Approximate completion time: 4 minutes    */
/*********************************************/

#include <stdio.h>

int main()
{
  int x;
  printf("Enter a number: ");
  scanf("%d", &x);
  if(x==0)
    {
      printf("The number is zero.\n");
    }
  else if(x>0)
    {
      printf("The number is positive!\n");
    }
  else
    {
      printf("The number is negative.\n");
    }
  return 0;
}
