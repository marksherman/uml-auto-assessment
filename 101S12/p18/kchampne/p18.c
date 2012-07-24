/****************************************/
/* Name: Kyle Champney                  */
/*                                      */
/* Program: p18                         */
/*                                      */
/* Estimated Completion Time: 5 mins    */
/****************************************/

#include <stdio.h>
#include <math.h>

int main(){

  printf("Please enter a floating point number representing the radius of a ");
  printf("circle: ");

  float r;
  scanf("%f", &r);

  float area = M_PI * r * r;

  printf("The area of the circle is %f\n", area);

  return 0;
}
