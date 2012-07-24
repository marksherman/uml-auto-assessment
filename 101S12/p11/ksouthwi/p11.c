/********************************************/
/* Programmer: Kevin Southwick              */
/*                                          */
/* Program 11: The abs Function             */
/*                                          */
/* Approximate completion time: 5  minutes  */
/********************************************/

#include <stdio.h>

#include <stdlib.h>

int main( ) {

  int variable ;

  printf( "input integer value: " );

  scanf( "%d" , &variable );

  variable = abs( variable );

  printf( "The absolute value of what you entered in is: %d \n" , variable );

  return 0;

}
