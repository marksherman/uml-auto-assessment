/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 6 Greater than zero                                                  */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(){
  int nin;
  printf("Please enter a number: ");
  scanf("%d", &nin);
  if(nin == 0)
    printf("Number is equal to zero.\n");
  else
    printf("Number is not equal to zero.\n");
  return 0;
}
