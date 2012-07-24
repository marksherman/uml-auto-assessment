/************************************************/
/*  Programmer: Zachary Durkee                  */
/*                                              */
/*  Program 18: Area of a Circle                */
/*                                              */
/*  Approximate completion time: 5 minutes      */
/************************************************/

#include <stdio.h>

#include <math.h>

int main( int argc, char *argv[] ){

  float r;

  float a;

  printf( "Enter the radius of the sphere:\n" );

  scanf( "%f", &r );

  a= M_PI*r*r;

  printf( "The area of the sphere:\n" );

  printf( "%f\n", a );

  return 0;

}
