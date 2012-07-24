/************************************************/
/* Programmer: Kyle White                       */
/* Program  17: Area of a Rectangle             */
/* Approximate completion time: 5 Minutes       */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  float h;
  float l;
  float area;

  h=0;
  l=0;
  area=0;

  printf ("\nLength:");

  scanf ("%f", &l);

  printf ("Height:");

  scanf ("%f", &h);

  area = l * h;

  printf ("Area = %f\n\n", area);

  return 0;

}
