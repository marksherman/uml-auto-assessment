/*************************************/
/* Name: Kyle Champney               */
/*                                   */
/* Program: p5                       */
/*                                   */
/* Estimated Completion Time: 15mins */
/*************************************/

#include <stdio.h>

int main(){

  int number;

  printf("Enter an integer: ");

  scanf("%d", &number);

  if (number > 100)
		printf("This number is bigger than 100\n");

  else
    printf("This number is not bigger than 100\n");

  return 0;
}
