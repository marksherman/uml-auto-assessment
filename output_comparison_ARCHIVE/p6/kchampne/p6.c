/*************************************/
/* Name: Kyle Champney               */
/*                                   */
/* Program: p6                       */
/*                                   */
/* Estimated Completion Time: 10 mins*/
/*************************************/

#include <stdio.h>

int main(){

  printf("Please enter an integer: ");

  int number;

  scanf("%d", &number);

  if (number == 0)
    printf("The number is equal to zero.\n");
  else 
    printf("The number is not equal to zero.\n");

  return 0;
}
