/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Non recursive Factorial          */
/*                                                */
/*Approximate completion time: 30 minutes         */
/**************************************************/
#include <stdio.h>
int factorial (int n);

int main (int argc, char* argv[]){
  int n;
 
  printf ("Enter an integer number: ");
  scanf ("%d", &n);
  while (n < 0){
    printf ("The integer should be >= 0, please enter again:");
    scanf ("%d",&n);
  }
 
  printf ("The factorial of the number is %d\n", factorial(n));
  return 0;
}

int factorial (int n){
  int product = 1;
  if (n == 0)
    return 1;
  while (n > 0){
    product = product * n;
    n--;
  }
  return product;
}
