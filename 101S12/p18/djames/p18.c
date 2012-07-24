/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 18: Area of a Circle                */
/*                                            */
/*Approximate completeion time: 10 minutes    */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int main(int argc, char* argv[]){  

  float r, pi;
  
  r = atof(argv[1]);

  pi = M_PI;

  printf( "the area of the circle is %f\n", r*r*pi );

  return 0;
}
