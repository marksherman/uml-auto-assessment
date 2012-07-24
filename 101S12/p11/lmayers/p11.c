/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: The abs Function                                             */
/*                                                                       */
/* Approximate completion time:5 minutes                                 */
/*************************************************************************/
#include <stdlib.h>
#include <stdio.h>
int main (int agrc, char *argv[] ) { 
  
  int x;
  
  printf("Please enter a single integer value.\n");  
  scanf("%d" , &x);
  
  abs(x);
  
  printf("The absolute value of the number you entered is: %d\n",abs(x));
  
  return 0;
  
}

