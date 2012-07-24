/**********************************************************/
/* Programmer: Joe LaMarca                                */
/* Program: p33 Recursive factorial                       */
/* Approximate time of completion:                        */
/**********************************************************/

#include <stdio.h>
#include <stdlib.h>

int factorial(int a);

int main(int argc, char* argv[]){

  int x;

  x=atoi(argv[1]);

  factorial(x);

  printf("The factorial for that number is: %d\n",factorial(x));

  return 0;
}

int factorial(int a){

  if(a==1)
    return 1;
  else
    return a*factorial(a-1);

}
