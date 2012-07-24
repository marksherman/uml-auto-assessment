/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 6: Equal to Zero?                 */
/*                                           */
/* Approximate completion time: 6 minutes    */
/*********************************************/

#include <stdio.h>

int main()
{
  int x;
  printf("Enter a number: ");
  scanf("%d", &x);
  if(x==0)
    {
      printf("The number is equal to zero.\n");
    }
  else
    {
      printf("The number is not equal to zero.\n");
    }
  return 0;
}
