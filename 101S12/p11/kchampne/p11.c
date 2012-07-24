/**************************************/
/* Name: Kyle Champney                */
/*                                    */
/* Program: p11                       */
/*                                    */
/* Estimated Completion Time: 10 mins */
/**************************************/

#include <stdio.h>
#include <stdlib.h>

int main(){

  printf("Please enter a single integer: ");

  int integer;
  scanf("%d", &integer);

  printf("The absolute value of the integer is: %d\n", abs(integer));

  return 0;
}
