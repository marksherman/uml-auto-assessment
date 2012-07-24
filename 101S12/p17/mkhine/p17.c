/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Area of a Rectangle              */
/*                                                */
/*Approximate completion time: 5 minutes         */
/**************************************************/
#include<stdio.h>
int main(int argc, char*argv[])
{
  float length;    /*declare the length, height and area */
  float height;
  float area;
  printf("Please enter the length and the height of a rectangle respectively.");
  scanf ("%f%f", &length, &height);   /*read the length and height entered */
  area= length*height;   /* calculate the area of rectangle */
  printf("The area of the rectangle is %f \n", area);  /*prints out the area of rectangle on the screen */
  return 0;
}

