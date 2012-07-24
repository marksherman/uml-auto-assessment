/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 17: Area of a Rectangle                                */
/* Approx Completion Time: 5 minutes                              */
/******************************************************************/

#include <stdio.h>

int main( int argc, char* argv [] ){
 
  float length;
  float height;
  float area;
 
  printf( "Enter the length of the rectangle: " );
    scanf( "%f", &length );
 
  printf( "Enter the height of the rectangle: " );
    scanf( "%f", &height );

  area = (length * height);
    printf( "The area of the rectangle is %f\n", area );
  
 return 0;
}

