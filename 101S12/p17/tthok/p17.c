/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 17 : Area of a Rectangle        */
/*                                         */
/* Approximate completion time: 60 minutes */
/*******************************************/

#include <stdio.h>
int main()
{
  float length;
  float height;
  float area;

  printf("Enter the length of a rectangle: ");
  scanf("%f", &length);
  printf("Enter the height of a rectangle: ");
  scanf("%f", &height);

  area = length * height;

  printf("The area of the rectangle: %f\n", area);

  return 0;  
}
