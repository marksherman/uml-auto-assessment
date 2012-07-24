/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 3: The Sum of Two Integers        */
/*                                           */
/* Approximate completion time: 7 minutes    */
/*********************************************/

#include <stdio.h>

int main()
{
  int x;
  int y;
  int sum;
  printf("Enter two numbers: ");
  scanf("%d %d", &x, &y);
  sum=x+y;
  printf("The sum of your two numbers is: %d\n", sum);
  return 0;
}
