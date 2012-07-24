/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 14 : Sine Fuction                                              */
/*                                                                        */
/* Approximate Time: 50 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main( int argc, char *argv[] ) {
 
  float x,y;

  x=atof (argv[1]);

  y= sin (x);

  printf("%f\n", y);

  return 0 ;

}
