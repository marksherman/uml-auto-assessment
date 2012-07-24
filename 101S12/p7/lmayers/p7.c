
/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Positive, Negativ or Zero                                    */
/*                                                                       */
/* Approximate completion time: 3 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main( int argc , char *argv[] ) {

  int x;
  
  printf("Please enter a single integer number:\n");
  
  scanf("%d", &x);
  
  if (x < 0) 

    printf("The number is negative.\n");
  
  else if (x == 0)

    printf ("The number is zero.\n");  

  else  if (x > 0)

    printf ("The number is positive..\n");  

  return 0;
  
}
