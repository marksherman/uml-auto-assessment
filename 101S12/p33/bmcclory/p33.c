/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #33: Recursive Factorial                      */
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

  printf("%d\n", num);

  return 0;
}

int factorial(int num){

  if(num == 1){
    return num;
  }
  else{
    return num * factorial(num - 1);
  }
}
