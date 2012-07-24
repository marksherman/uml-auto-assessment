/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 18 : Area of a Circle           */
/*                                         */
/* Approximate completion time: 60 minutes */
/*******************************************/

#include <stdio.h>
#include <math.h>
int main ()
{
  float radius;
  float M_pi;
  float area;
  M_pi = 3.14159265358979323846;

  printf("Enter the value of the radius: ");
  scanf("%f", &radius);
  
  area = radius * radius * M_pi;
  printf("The area of the circle: %f\n", area);

  return 0;  
}
