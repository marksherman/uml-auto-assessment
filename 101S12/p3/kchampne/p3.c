/*************************************/
/* Name: Kyle Champney               */
/*                                   */
/* Program: p3                       */
/*                                   */
/* Estimated Completion Time: 10mins */
/*************************************/

#include <stdio.h>

int main(){

  int x, y;

  printf("Please enter two integers seperated by a space: ");

  scanf("%d %d", &x, &y);

  int sum = x + y;

  printf("The sum of the two integers you entered is: %d\n", sum);

  return 0;
}
