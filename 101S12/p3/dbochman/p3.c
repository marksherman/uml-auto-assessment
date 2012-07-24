/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 3:    Sum of Two values                      */
/* Time:         10 mintues                             */
/********************************************************/

#include <stdio.h>

int main ( int argc, char *argv[] ) {

  int x;

  int y;

  int sum;

  printf( "\nPlease enter two values to be added:\n\n");

  scanf( "%d", &x);

  printf( "+\n");

  scanf( "%d", &y);

  sum= (x+y);

  printf( "\nThe sum of the two values is %d\n\n", sum);

  return 0 ;

}
