/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 18: Area of a circle            */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#define M_PI 3.14159265358979323846

int main (int argc, char* argv [])
{

  float r;
  
  r = atof(argv[1]);
  printf("%f\n", M_PI * r * r);
  printf("%f\n", r);

  return 0;

}
