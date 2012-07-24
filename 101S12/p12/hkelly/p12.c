/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 12: Using the sqrt Function              */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>
#include <math.h>

int main( int argc, char* argv[] ){

  float x, answer;
 
  printf("Enter a number:\n");
  scanf("%f", &x);
  answer = sqrt(x);

  printf("The square root of that number is: %f\n", answer);
  
  return 0;
}
