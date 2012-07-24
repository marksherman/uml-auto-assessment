/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 2:    The scanf Function                     */
/* Time:         4 minutes                              */
/********************************************************/
#include <stdio.h>
int main () {

  int x;

  printf( "\nEnter a value for x:\n");

  scanf( "%d",&x);

  printf( "The value you entered for x is %d.\n\n", x);

  return 0 ;

}
