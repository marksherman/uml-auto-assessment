/********************************************/
/* Programmer: Harrison Kelly               */
/*                                          */
/* Program 3: Sum of Two Values             */
/*                                          */
/* Approximate completion time: 5 Minutes   */
/*******************************************/

#include <stdio.h>

int main () {

  int vala, valb, sum;

  printf( "\nEnter a value for A, hit enter and then enter a value for b and hit enter:\n");
  scanf( "%d\n%d", &vala,&valb );

  sum = vala + valb;
  printf( "The sum of these two numbers is: %d\n", sum);
  
  sum = 0;
  return 0;
}
