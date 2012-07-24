/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 18: Area of a Circle                                           */
/*                                                                        */
/* Approximate Time: 20 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>
#include <math.h>

int main( int argc, char *argv[] ) {

  float x;

  printf("\nPlease Enter a Number:");

  scanf("%f", &x);

  printf("\nThe area of a cirle with the radius of %f is %f\n\n",x, x*x*M_PI);

  return 0 ;

}
