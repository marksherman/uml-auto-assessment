/********************************************/
/* Programmer: Kevin Southwick              */
/*                                          */
/* Program 12: The sqrt Function            */
/*                                          */
/* Approximate completion time: 15  minutes */
/********************************************/

#include <stdio.h>

#include <math.h>

int main( ) {

  float x ;

  printf( "input floating point number: " );

  scanf( "%e" , &x );

  x = sqrt( x );

  printf( "The square root is: %e \n" , x );

  return 0;

}
