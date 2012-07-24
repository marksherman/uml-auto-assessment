/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Non-recursive Factorial                                          */
/*                                                                           */
/* Approximate completion time: 30 minutes                                   */
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
  int f=1;
  int i;
  for (i=1; i<=n; i++){
  /* runs loop until n is reached*/
    f = f*i;
    /* multiplies cummulative product of the numbers by the next value of i */
  }
  return f;
  /* returns the cummulative product, also known as the factorial, to the main function */
}
