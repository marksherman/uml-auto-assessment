/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 2: The scanf function                                                */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(){
  int nin;
  printf("Enter a number\n");                   /* Prompt user for input number */
  scanf("%d", &nin);                            /* Save input to address of nin */
  printf("You entered %d\n", nin);              /* Print out saved number       */
  return 0;
}
