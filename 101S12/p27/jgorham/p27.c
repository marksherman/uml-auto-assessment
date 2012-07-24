/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 27 Reverse                                                           */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int i = 0;
  int input[10];
  printf("Please input 10 numbers:\n");
  for(i = 0 ; i < 10 ; i++)
    scanf("%d", &input[i]);
  for(i = 9 ; i >= 0 ; i--)
    printf("%d ", input[i]);
  printf("\n");
  return 0;
}
