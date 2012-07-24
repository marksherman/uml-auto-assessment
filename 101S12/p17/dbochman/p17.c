/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 17:   Area of a Rectangle                    */
/* Time:         10 minutes                             */
/********************************************************/
#include <stdio.h>

int main ( int argc, char *argv[] ) {

  float x,y,a;

  printf("Please enter both the dimensions of a Rectangle\n");

  scanf("%f %f",&x,&y);

  a=x*y;

  printf("The area of the rectangle is %f units\n",a);
}
