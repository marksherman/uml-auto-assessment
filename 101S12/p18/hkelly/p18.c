/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 18: Area of a Circle                     */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>
#include <math.h>

int main( int argc, char* argv[] ){

  int r;

  printf("\nEnter a radius:");
  scanf("%d", &r);

  printf("\nThe area of a circle with that radius is: %f\n", r*r*M_PI);

  return 0;
}
