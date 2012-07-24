#include <stdio.h>
#include <math.h>
#include <stdlib.h>
int main(int argc, char* argv[]){
  
  float x;
 
  x = atof(argv[1]);
  printf("%f\n", sin(x));

  return 0;

}
