/***************************************************/
/*  Programmer: Zachary Durkee                     */
/*                                                 */
/*  Program 12: Using the sqrt Function            */
/*                                                 */
/*  Approximate completion time: 30 minutes        */
/***************************************************/


#include <math.h>

#include <stdio.h> 

int main( int argc, char* argv[] )

{

  float x;

  float y;

  printf( "Enter a Number:\n" );

  scanf( "%f", &x );

  printf( "Square-root of Number Entered:\n" );

  y= sqrt( x );

  printf( "%f\n", y );

  return 0;

}
