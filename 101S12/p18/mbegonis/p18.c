/******************************************************************************************************************************/
/*                                                                                                                            */
/*  Mike Begonis                                                                                                              */
/*  Program p18                                                                                                               */
/*  This program will take the radius of a circle inputed by the user and will calculate the area and print it to the screen. */
/*  Aprox Completion Time: 15 minutes                                                                                         */
/*                                                                                                                            */
/******************************************************************************************************************************/


#include <stdio.h>
#include <math.h>

int main(int argc, char* argv[]){

  double r,a;
  printf("Please enter the radius of your circle:\n");
  scanf("%lf",&r);
  a=r*r*M_PI;
  printf("Thank you very much, the area of the circle is %lf.\n",a);

  return 0;
}
