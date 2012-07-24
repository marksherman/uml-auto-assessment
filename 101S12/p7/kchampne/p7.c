/*************************************/
/* Name: Kyle Champney               */
/*                                   */
/* Program: p7                       */
/*                                   */
/* Estimated Completion Time: 15 mins*/
/*************************************/

#include <stdio.h>

int main(){

  printf("Please enter a single integer: ");

  int number;

  scanf("%d", &number);

  if (number > 0)
    printf("The number is positive\n");
  else if (number < 0)
    printf("The number is negative\n");
  else if (number == 0)
    printf("The number is zero\n");
  else
    return 0;

  return 0;
}
