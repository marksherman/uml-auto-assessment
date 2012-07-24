/*****************************************************/
/*  Programmer: Zachary Durkee                       */
/*                                                   */
/*  Program 17: Area of a Rectangle                  */
/*                                                   */
/*  Approximate completion time:  5 minutes          */
/*****************************************************/



#include <stdio.h>

int main( int argc, char *argv[] ){

  float x;

  float y;

  float z;

  printf( "Enter the height and length of the rectangle, respectively:\n" );

  scanf( "%f %f", &x, &y );

  z=x*y;

  printf( "The area of the rectangle is:\n" );

  printf( "%f\n", z );

  return 0;

}
