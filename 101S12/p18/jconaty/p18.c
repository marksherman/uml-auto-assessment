/*
Jake Conaty
project 18
*/


#include <stdio.h>
#include <math.h>


int main(int argc, char* argv[]){

  double r, a;

  printf("This program will give you the area of a circle with a radis u give me. Insert the radius:\n");
  scanf("%lf", &r);


  a=r*r*M_PI;

  printf("The area of your circle is %lf.\n", a);

  return 0;

}
