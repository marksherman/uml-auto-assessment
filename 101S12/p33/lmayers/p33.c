

/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Recursive Factorial                                          */
/*                                                                       */
/* Approximate completion time: 45 minutes                               */
/*************************************************************************/
#include <stdio.h>
int fact (int n ) ;

int main (int argc , char *argv[] ) {

  int n, sum = 1;

  printf("Please enter a single integer value for n:\n");
  scanf("%d", &n);

  sum = fact ( n );

  printf("The product is %d\n",sum);
  return 0;
}


int fact (int n ) {
  
  if ( n == 0)return 1;
  if (n == 1)return 1;
  return n * fact(n-1);
}
