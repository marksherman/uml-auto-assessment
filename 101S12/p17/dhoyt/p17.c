/******************************/
/* Programmer: David Hoyt     */
/* Program: Area of Rectangle */
/* Time: 10min                */

#include <stdio.h>

int main(){

  float l,h;

  printf( "Enter the length of your rectangle:" );

  scanf( "%f", &l );

  printf( "Enter the height of your rectangle:" );

  scanf( "%f", &h );

  printf( "Area: %f\n", l*h );

  return 0;

}
