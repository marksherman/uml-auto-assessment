/*

Mike Begonis
Program p12

This program takes the a number given by the user and returns the square root of it.

*/


#include <stdio.h>
#include <math.h>

int main (int argc, char* argv[]){
  
  double x;
  printf("If you enter a number right now, I will find the square root of it for you.  Aren't I nice?\n");
  scanf("%lf", &x);

  x=sqrt(x);

  printf("The square root of this number is %lf\n", x);



  return 0;

}
