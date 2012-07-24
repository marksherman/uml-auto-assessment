/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 14 Sine Function                                                     */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main(int argc, char* argv[]){
  float nin = atof(argv[1]);              /*Assign input to variable*/
  float nout;
  nout = sin(nin);                        /*Calculate sine*/
  printf("The sine is: %e\n", nout);
  return 0;
}
