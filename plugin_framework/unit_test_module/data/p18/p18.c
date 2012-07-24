
/***********************************/
/* Author: James DeFilippo         */ 
/* Title: Area of a Circle         */
/* Approximate Time: 15 minutes    */ 
/***********************************/

#include <stdio.h>
#include <math.h> /* the value for PI is contained here */ 

int main ( int argc, char* argv[] ) 
{
  float r; /* float or double: an arbitrary choice for this exercise */ 
  float A; 
  printf("Please enter a value for radius.\n"); /* user-friendly prompt */ 
  scanf("%f", &r); 
  A=M_PI*r*r; /* M_PI is defined as pi in math.h */ 
  printf("The area of a circle of %f radius is %f.\n", r, A); 
  return 0; 
} 
