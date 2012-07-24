/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 28 Digit Sum                                                         */
/*                                                                              */
/* Approximate Completion Time:  20min                                          */
/********************************************************************************/

#include <stdio.h>

int sumer(int in);

int main(int argc, char* argv[]){
  FILE* fin = fopen(argv[1], "r");
  int input = 0;
  int sum = 0;
  fscanf(fin, "%d", &input);
  sum = sumer(input);
  printf("Sum of the integer's digits = %d\n", sum);
  return 0;
}

int sumer(int in){
  int digitcount = 0;
  int sum = 0;
  int digit = 0;
  int i = 0;
  while(i > 0){
    digitcount++;
    i = i/10;
  }
  while(in > 0){
    digit = in % 10;
    in = in/10;
    sum = sum + digit;
  }
  return sum;
}
