#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main(int argc, char* argv[]){

  double x, y;

  x=atof(argv[1]);

  y=sin(x);

  printf("The sin of your number is %lf.\n",y);

  return 0;
}
