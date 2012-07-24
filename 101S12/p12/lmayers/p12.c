/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Using the sqrt Function                                      */
/*                                                                       */
/* Approximate completion time:3 minutes                                 */
/*************************************************************************/
#include <math.h>
#include <stdio.h>
int main (int agrc, char *argv[] ){
  
  float x;
  
  printf("Please enter a floating point number:\n");  
  scanf("%f" , &x);
  
  printf("The square root of the number you entered is: %f\n",sqrt(x));
  
  return 0;
  
}

