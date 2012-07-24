/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 32 Iterative Factorial                                               */
/*                                                                              */
/* Approximate Completion Time:  10 min                                          */
/********************************************************************************/

#include <stdio.h>

int factorial(int n);

int main(int argc, char* argv[]){
  int input = 0;
  int fact = 1;
  printf("Enter integer: ");
  scanf("%d", &input);
  fact = factorial(input);
  printf("Factorial = %d\n", fact);
  return 0;
}

int factorial(int n){
  int fact = 1;
  int i = 0;
  if( n == 0 || n == 1)                   /*Added base case*/
    return 1;
  for( i = n ; i > 0 ; i--){
    fact = n * fact;
    n--;
  }
  return fact;
}
