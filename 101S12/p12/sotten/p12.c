/**************************************/
/* Programmer: Samantha M. Otten      */
/*                                    */
/*Program 12: Using the sqrt function */
/*                                    */
/*Approx. Completion Time: 20mins     */
/*                                    */
/**************************************/

#include <stdio.h>
#include <math.h>

int main(){

  float a;
  printf("Select an integer from the keyboard\n");
  scanf("%f", &a);
  printf("The square root of the selected integer equals:%f\n", sqrt(a));
  return 0;

}
