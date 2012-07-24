/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 18: Area of a Circle                                   */
/* Approx Completion Time: 15 minutes                             */
/******************************************************************/

#include <stdio.h>
#include <math.h>

int main( int argc, char* argv [] ){
 
  float radius;
  float pi = M_PI;
  float area;

  printf( "Enter the radius of the circle: " );
    scanf( "%f", &radius );

  area = (radius * radius)* pi; 
    printf( "The area of the circle is %f\n", area );
  
 return 0;
}

