/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 11 Abs function                                                      */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(){
  int nin= 0;
  printf("Please enter a number ");
  scanf("%d", &nin);
  nin = abs(nin);
  printf("Absolute value is: %d\n", nin);
  return 0;
}
