/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 17: Area of a Rectangle                                        */
/*                                                                        */
/* Approximate Time: 20 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  float x , y;

  printf("\nPlease enter two numbers.\n");

  printf("\nEnter the first number:");

  scanf("%f", &x);

  printf("Enter the second number:");

  scanf("%f", &y);

  printf("\nThe Area of a Rectangle that is %f by %f is:%f\n\n", x , y , x*y);

  return 0 ;

}
