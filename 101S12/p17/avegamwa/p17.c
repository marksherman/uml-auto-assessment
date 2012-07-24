/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p17:Area of Rectangle          */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int main(int argc, char* argv[])
{

  float height, length;
  double area;

  printf( "Please enter the Height of Rectangle:\n" );
  scanf( "%f", &height );

  printf( "Please enter the Length of Rectangle:\n" );
  scanf( "%f", &length );

  area = height * length;
  
  printf( "The area of the rectangle is: %f\n", area );
  

  return 0;

}
