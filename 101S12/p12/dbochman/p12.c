/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 12:   Using the sqrt Function                */
/* Time:         10 minutes                             */
/********************************************************/

#include <stdio.h>
#include <math.h>

int main (int argc, char*argv[]){

  float n, r;

  printf("Please enter a number\n");
   
  scanf("%f",&n);
 
  r=sqrt(n);

  printf("The square root of the number you entered is %f\n",r);

  return 0;
}
