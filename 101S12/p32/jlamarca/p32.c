/******************************************************/
/* Programmer: Joe LaMarca                            */
/* Program: p32 non-recursive factorial               */
/* Approximate time of completion: 20 min             */
/******************************************************/

#include <stdio.h>
#include <stdlib.h>

int factorial(int a);

int main(int argc, char* argv[]){

  int x;

  x=atoi(argv[1]);

  factorial(x);

  printf("The factorial of that number is: %d \n",factorial(x));

  return 0;
}

int factorial(int a){

  int fact;
  int i;

  fact=1;
  for(i=1;i<=a;i++)
    fact=i*fact;

  return fact;
}
