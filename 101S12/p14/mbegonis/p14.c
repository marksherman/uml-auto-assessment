/*

Mike Begonis
Program p14

This program reads a number from the command line and prints the trig sin value of it.

*/

#include <stdlib.h>
#include <math.h>
#include <stdio.h>

int main(int argc, char* argv[]){
  
  
  double x=sin(atof(argv[1]));
  double y=atof(argv[1]);


  printf("The sin of %lf is %lf.\n",y,x);



  return 0;
}
