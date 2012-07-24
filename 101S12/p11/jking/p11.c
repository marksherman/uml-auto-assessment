/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 11: The abs Function                                   */
/* Approx Completion Time: 5 Mintues                              */
/******************************************************************/

#include<stdio.h>
#include<stdlib.h>

int main( int argc, char* argv [] ){
 
  int x;

  printf( "Enter any integer:\n" );
  scanf( "%d", &x );
  x = abs( x );
  printf( "The Absolute Value of the integer you entered is:\n%d\n", x );

  return 0;
}

