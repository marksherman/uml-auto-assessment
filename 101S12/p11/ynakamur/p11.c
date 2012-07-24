/*************************************************************/
/* Programmer: Yasutoshi Nakamura                            */
/*                                                           */
/* Program 11: The abs Function                              */
/*                                                           */
/* Approximate completion time: 10 minutes                   */
/*************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char *argv[] ) {

  int number;

  printf( "Please input a single integer.\n" );

  scanf( "%d", &number );

  number = abs( number );

  printf( "The absolute value of the inputted number is: %d.\n", number );

  return 0;

}
