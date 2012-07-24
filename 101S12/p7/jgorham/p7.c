/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 7 +, - or 0                                                          */
/*                                                                              */
/* Approximate Completion Time:                                                 */
/********************************************************************************/

#include <stdio.h>

int main(){
  int nin;
  printf("Please enter a number: ");
  scanf("%d", &nin);
  if(nin == 0)
    printf("The number is zero.\n");
  else if(nin > 0)
    printf("The number is positive.\n");
  else if(nin < 0)
    printf("The number is negative.\n");
  return 0;
}
