/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Area of a Circle                 */
/*                                                */
/*Approximate completion time: 5 minutes         */
/**************************************************/
#include<stdio.h>
#include<math.h>
#define pi 3.14159265358979323846  /* pi */
int main(int argc, char*argv[])
{
  float radius;    /*declare the radius of the circle */
  float area;      /*declare the area of the circle */
  printf("Please enter the radius of a circle.");
  scanf ("%f", &radius);   /*read the radius of the circle entered */
  area= pi*radius*radius ;   /* calculate the area of circle */
  printf("The area of the circle is %f \n", area);  /*prints out the area of\
							 circle on the screen */
  return 0;
}


