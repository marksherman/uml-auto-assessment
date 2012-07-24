/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #32: Non-Recursive Factorial                  */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int factorial(int num);

  int main(int argc, char* argv[]){

  int num;

  printf("Type a positive integer: ");
  scanf("%d", &num);

  int factorial(int num);

  printf("%c\n", fact);

  return 0;
}

int factorial(int num){

  int i;

  int fact = 1;

  int temp = num;

  for(i = 0; i < temp; i++){
    fact = fact * num;
    num--;
  }
  return fact;
}
