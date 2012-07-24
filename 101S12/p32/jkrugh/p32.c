/*************************************************************/
/* Programmer: Jeremy Krugh                                  */
/*                                                           */
/* Program 32: Non-recursive Factorial                       */
/*                                                           */
/* Approximate completion time: 25 minutes                   */
/*************************************************************/

#include <stdio.h>
#include <stdlib.h>

int fact(int n);

int main(int argc, char* argv[]){

  int x;

  x = atoi(argv[1]);

  fact(x);

  printf("The factorial is: %d\n",fact(x));

    return 0;
}

int fact(int n){

  int f;
  int i;

  f = 1;
  for(i = 1; i <= n; i++){
    f = f * i;
  }
  return f;
}

