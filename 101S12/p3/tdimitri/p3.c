/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 1: Sum of two Values                  */
/* Approximate completion time: 45 mins          */
/*************************************************/
#include <stdio.h>
int main() {
  int x, y, sum;
  printf( "Enter two numbers: ");
  scanf("%d %d",&x,&y);
  sum = x + y;
  printf( "The sum is %d\n", sum);
  return 0;
}
