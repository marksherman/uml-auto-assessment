/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Area of a Rectangle              */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  float length, height, sum;
  
  printf( "Enter the length and the height of the rectangle: " );
  scanf( "%f%f", &length, &height );

  sum = length * height;

  printf( "The area is %f.\n", sum );

  return 0;
}
