/******************************************************/
/* Programmer: Nathan Goss                            */
/*                                                    */
/* Program 12: Using the sqrt Function                */
/*                                                    */
/* Approximate completion time: 5 minutes             */
/******************************************************/

#include <stdio.h>
#include <math.h>


int main(int argc, char* argv[])
{
  float inval;

  printf("Input a floating point number: ");
  scanf("%f", &inval);

  printf("%f\n", sqrt(inval));

  return 0;
}
