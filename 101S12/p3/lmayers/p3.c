/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Sum of Two Values                                            */
/*                                                                       */
/* Approximate completion time: 3 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main( int argc , char *argv[] ) {
  
  int sum, y,  x;
  
  printf("Please enter two integer values:");
  
  scanf("%d %d", &y ,&x);
  
  sum = y * x;
  
  printf("The sum of the two integers is: %d\n",sum );
  
  return 0;
  
}
