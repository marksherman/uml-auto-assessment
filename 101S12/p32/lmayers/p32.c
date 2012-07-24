

/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Non-recursive Factorial                                      */
/*                                                                       */
/* Approximate completion time: 45 minutes                               */
/*************************************************************************/
#include <stdio.h>

int main (int argc , char *argv[] ) {

  int x, sum = 1,i;

  printf("Please enter a single integer value for x:\n");
  scanf("%d", &x);

  
  if ( x > 0 ) {
  for( i = x; i >= 2; i-- )
    sum *= i;
  }else
    sum = 1;
 
  printf("The product is %d\n",sum);
  
    return 0;
}
