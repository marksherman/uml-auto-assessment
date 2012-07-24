/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 18: Area of a Circle                                                 */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>
#include <math.h>

int main(int argc, char* argv[]){
  float radius = 0;
  float area = 0;
  printf("Enter radius: ");
  scanf("%f", &radius);
  area = radius * radius * M_PI;
  printf("Area of the circle is %f.\n", area);
  return 0;
}
