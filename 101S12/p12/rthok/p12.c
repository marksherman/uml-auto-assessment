/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 13: Using the sqrt Function                                    */
/*                                                                        */
/* Approximate Time: 60 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>
#include <math.h>

int main( int argc, char *argv[] ) {

  int x;

  printf("\nPlease Enter a Number:");

  scanf("%d", &x);

  x= sqrt(x);

  printf("\n%d\n\n", x);

  return 0 ;

}
