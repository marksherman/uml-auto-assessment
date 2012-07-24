/*********************************************************************/
/* Programmer: Jeremy Krugh                                          */
/*                                                                   */
/* Program 33: Recursive Factorial                                   */
/*                                                                   */
/* Approximate completion time: 30 minutes                           */
/*********************************************************************/

#include <stdio.h>
#include <stdlib.h>

int fact(int n);

int main(int argc, char* argv[]){

  int x;

  x = atoi (argv[1]);
  fact(x);

  printf("The factorial is: %d\n", fact(x));

  return 0;
}

int fact( int n){
  if (n == 1){
  return 1;
  }
  else 
  return n * fact(n-1);
}
