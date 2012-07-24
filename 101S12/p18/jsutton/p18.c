/**********************************************/
/* Programmer: Joanna Sutton                  */
/*                                            */
/* Assignment: Area of a Circle               */
/*                                            */
/* Approximate Completion Time: 15 minutes    */
/**********************************************/
#include <stdio.h>
#include <math.h>

int main(int argc, char*argv []){

  float r;
  float a;

  printf("Please enter the radius of a circle.\n");
  scanf("%f",&r);
  a=r*r*M_PI;
  printf("The area of that circle is: %f\n",a);

  return 0;

}
