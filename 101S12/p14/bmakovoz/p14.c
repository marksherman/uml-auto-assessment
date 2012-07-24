/*********************/
/*  Betty Makovoz    */
/*  Sine Function    */
/*  13 minutes       */
/*********************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main (int argc, char*argv[]){
    float x;
    x=atof(argv[1]);
    x=sin(x); 
   printf(" The sine is:%f\n",sin(x));
    return 0;
  }
