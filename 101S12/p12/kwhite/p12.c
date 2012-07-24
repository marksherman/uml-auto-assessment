/************************************************/
/* Programmer: Kyle White                       */
/* Program  12: Using the sqrt function         */
/* Approximate completion time: 20 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main (int argc, char* argv [])

{

  int x;
  double y;

  printf("\nPlease enter a number:");

  scanf( "%d", &x);

  x=(double) x;

  y= sqrt(x);

  printf( "Square Root = %f\n\n", y);

  return 0;

}
