/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 12: Using the sqrt Function         */
/*                                            */
/*Approximate completeion time: 10 minutes    */
/**********************************************/

#include <stdio.h>
#include <math.h>
int main(int argc, char* argv[]){  

  float x;

  printf( "enter a floating point number\n" );
  
  scanf( "%f", &x );

  x = sqrt( x );
  
  printf( "the square root of the entered float point number is %f\n", x);

  return 0;
}
