/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 17: Area of Rectangle   */
/*                                */
/*Approx. Completion Time: 20mins */
/*                                */
/**********************************/

#include <stdio.h>
#include <math.h>

int main(int argc, char*argv[]){
  float a,b;
  printf("Select two integers from the keyboard to represent the dimensions of a rectangle:\n");
  scanf("%f%f",&a,&b);
  printf("The area of the rectangle is:%f\n", (a*b) );
  return 0;
}

