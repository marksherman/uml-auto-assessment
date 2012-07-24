/*****************************************************************************/
/* Programmer: Erin R. Graceffa                                              */
/*                                                                           */
/* Program: Sum of Two Values                                                */
/*                                                                           */
/* Approximate completion time: 35                                           */
/*****************************************************************************/

#include <stdio.h>
int main (){
  int integer1;
  int integer2;
  int sum;
  printf("Please enter the two numbers you wish to sum: \n");
  scanf("%d %d", &integer1, &integer2);
  sum = integer1 + integer2;
  printf("\nThe sum of those two numbers is: %d \n", sum);
  return 0;
}
