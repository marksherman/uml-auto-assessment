/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 12: Using the sqrt Function                          */
/*                                                              */
/* Approximate completion time: 10 minutes                      */
/****************************************************************/

#include <stdio.h>
#include <math.h>

int main( int argc, char *argv[] ) {

  float number;

  printf( "Please enter a positive floating point number.\n" );

  scanf( "%f", &number );

  number = sqrt( number );

  printf( "The square root of the inputted number is: %f\n", number );

  return 0;

}
