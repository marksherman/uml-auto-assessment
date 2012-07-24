
/********************************/
/* Author: James DeFilippo      */
/* Title: Area of a Rectangle   */
/* Approximate Time: 15 minutes */
/********************************/

#include<stdio.h>
int main ( int argc, char *argv[] ) 
{
  double l, h, A; /* chose type double, could be of type float as well */ 
  printf("Hi! Please enter values for length and width.\n"); /* user-friendly prompt */ 
  scanf("%lf %lf", &l, &h); 
  A = l*h; 
  printf("The area of the rectangle with the dimensions you entered is %f.\n", A); /* interpretation of results */ 
  return 0; 
}
