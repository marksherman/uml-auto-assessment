/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 14: Sine Function                   */
/*                                            */
/*Approximate completeion time: 12 minutes    */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int main(int argc, char* argv[]){  

  float x;

 x = atof( argv[1] );

  x = sin ( x );

  printf( "%f\n", x );

  return 0;
}
