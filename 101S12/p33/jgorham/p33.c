/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 32 Recursive Factorial                                               */
/*                                                                              */
/* Approximate Completion Time:  10 min                                         */
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
  if( n == 0 || n == 1)                              /*Base case, have to go add this to 32...*/
    return 1;
  else
    return (n * factorial( n - 1 ));
}
