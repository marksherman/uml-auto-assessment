/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 38 recursive Digit Sum                                               */
/*                                                                              */
/* Approximate Completion Time:  20min                                          */
/********************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int sumer(FILE* fin, int sum, int* c_ptr);

int main(int argc, char* argv[]){
  FILE* fin = fopen(argv[1], "r");
  int sum = 0;
  int c = 0;
  int* c_ptr = &c;
  sum = sumer(fin, sum, c_ptr);
  sum = sum - 48*(c-1) - 10;
  printf("Sum of the integer's digits = %d\n", sum);
  return 0;
}


int sumer(FILE* fin, int sum, int* c_ptr){
  int input = 0;
  if( (input = fgetc(fin)) != EOF){
    sum = sum + input + sumer(fin, sum, c_ptr);
    *c_ptr += 1;
  }
  return sum;
}
