/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 17: Area of a Rectangle                  */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  float length;
  float height;

  printf("\nEnter a length:");
  scanf("%f", &length);

  printf("Enter a height:");
  scanf("%f", &height);

  printf("\nThe area of the rectangle is: %f\n", height*length);

  return 0;
 
}
