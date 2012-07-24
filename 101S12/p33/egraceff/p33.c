/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Recursive Factorial                                              */
/*                                                                           */
/* Approximate completion time: 10 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int factorial( int n);
int main( int argc, char *argv[] )
{
  int n;
  int x;
  printf("Please enter the positive n value for the factorial you wish to calculate:\nn = ");
  /* prompts the user for the factorial, n, to be calculated */
  scanf("%d", &n);
  /* scans in this n value from standard input */
  x = factorial(n);
  /* calls the factorial function to calculate the nth factorial */
  printf("The value of %d factorial is %d.\n", n, x);
  /* prints the value of the factorial calculated */
  return 0 ;
}
int factorial( int n){
  if (n == 0 || n == 1){
    return 1;
  /* returns the value of 1 to main for the two base cases of n =  0 and n = 1 */
  }
  else{
    return (n*factorial(n-1));
  /* otherwise, the factorial function is called until the base case is reached */
  }
}
