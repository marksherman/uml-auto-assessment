/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: The scanf Function                                           */
/*                                                                       */
/* Approximate completion time: 3 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main( int argc , char *argv[] ) {
  
  int y;
  
  printf("Please enter an integer value:");
  scanf("%d", &y);
  
  y = printf("The value you enter is: %d\n",y);
  
  return 0;
  
}
