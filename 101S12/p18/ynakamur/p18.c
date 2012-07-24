/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 18: Area of a Circle                                 */
/*                                                              */
/* Approximate completion time: 10 minutes                      */
/****************************************************************/

#include <stdio.h>
#include <math.h>

int main( int argc, char *argv[] ) {

  float radius, area;

  printf( "Please enter a value for the radius.\n" );

  scanf( "%f", &radius );

  area = M_PI * radius * radius;

  printf( "The area of the circle is %f units squared.\n", area );

  return 0;

}
