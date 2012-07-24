/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 17: Area of a Rectangle                              */
/*                                                              */
/* Approximate completion time: 10 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  float length, height, area;

  printf( "Please enter the value for length.\n" );

  scanf( "%f", &length );

  printf( "Please enter the value for height.\n" );

  scanf( "%f", &height );

  area = length * height;

  printf( "The area of the rectangle is equal to %f units squared.\n", area );

  return 0;

}
