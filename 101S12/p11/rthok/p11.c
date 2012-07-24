/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 11: The abs Function                                           */
/*                                                                        */
/* Approximate Time: 75 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char *argv[] ) {

  int x;

  printf("\nPlease Enter a Number:");

  scanf("%d", &x);

  x= abs(x);

  printf("%d\n\n", x);

  return 0 ;

}
