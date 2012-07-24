/*******************************************/
/* Programmer: Joanna Sutton               */
/*                                         */
/* Assignment Name: Using the sqrt Function*/
/*                                         */
/* Approximate Completion Time: 5 minutes  */
/*******************************************/

#include <stdio.h>
#include <math.h>

int main (int argc, char* argv[]){
  float x;

  printf("Please enter a non-negative number.\n");
  scanf("%f)",&x);
  x=sqrt(x);
  printf("The square root of that number is: %f\n",x);

  return 0;
}
