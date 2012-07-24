/***************************/
/*      Betty Makovoz      */
/*     Area of a circle    */
/*        17 minutes       */
/***************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>
int main (int argc, char*argv[]){
  float r, pi ;
  r=atof(argv[1]);
  pi=M_PI;
  printf("The area of a Circle %f\n",r*r*pi);
  return 0;
}
