/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 37 Digit Sum                                                         */
/*                                                                              */
/* Approximate Completion Time:  20min                                          */
/********************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){
  FILE* fin = fopen(argv[1], "r");
  int input = 0;
  int sum = 0;
  int c = 0;
  while((input = fgetc(fin)) != EOF){
    sum = sum + (input);
    c++;
  }
  sum = sum - 48*(c-1) - 10;
  printf("Sum of the integer's digits = %d\n", sum);
  return 0;
}
