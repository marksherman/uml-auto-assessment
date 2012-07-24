
/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Equal to Zero                                                */
/*                                                                       */
/* Approximate completion time: 3 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main( int argc , char *argv[] ) {

  int x;
  
  printf("Please enter a single integer number:\n");

  scanf("%d", &x);
  
  if (x < 0)
    printf("The number is not equal to zero.\n");
 
  else if (x == 0)

    printf ("The number is equal to zero.\n");

  else if (x > 0)

    printf("The number is not equal to zero.\n");

  return 0;
  
}
