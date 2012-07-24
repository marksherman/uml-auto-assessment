/**********************************************************************************/
/* Programmer: Lisa Mayers                                                        */
/*                                                                                */
/* Program: Area of a Rectangle                                                   */
/*                                                                                */
/* Approximate completion time: 10 minutes                                        */
/**********************************************************************************/
#include <stdio.h>

int main (int argc, char* argv[] ) {

  float L, H , area = 0;

  printf("Please enter two floating point numbers for length and height:\n");

  scanf("%f %f", &L , &H );

  area = (L * H );

  printf("The area of the rectangle is: %f\n", area );

  return 0;
}

