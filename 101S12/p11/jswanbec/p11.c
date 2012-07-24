/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 11: The abs Function              */
/*                                           */
/* Approximate completion time: 12 minutes   */
/*********************************************/

#include <stdio.h>
#include <stdlib.h>

int main()
{
  int x;
  int y;
  printf("Enter an integer: ");
  scanf("%d",&x);
  y=abs(x);
  printf("The absolute value of your integer is: %d\n",y);
  return(0);
}
