/************************************************/
/* Programmer: Kyle White                       */
/* Program  11: The abs function                */
/* Approximate completion time: 15 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>
#include <stdlib.h>
int main (int argc, char* argv [])

{

  int x;

  printf ( "\nPlease Enter a Number:" );

  scanf ( "%d", &x );

  x= abs( x );

  printf( "The absolute value of the number is %d\n\n", x );

  return 0;

}
