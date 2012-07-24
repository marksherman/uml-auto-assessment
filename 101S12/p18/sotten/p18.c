/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 18: Area of a Circle    */
/*                                */
/*Approx. Completion Time: 20mins */
/*                                */
/**********************************/

#include <stdio.h>
#include <math.h>

int main(int argc, char*argv[]){

  float r;

  printf("Choose an integer value to represent the radius of the circle:\n");
  
  scanf("%f", &r);

  printf("The area of the circle is:%f\n", r*r*M_PI);

  return 0;

}

