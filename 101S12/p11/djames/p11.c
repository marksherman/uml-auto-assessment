/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 11: The abs Function                */
/*                                            */
/*Approximate completeion time: 15 minutes    */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[]){  

  int x;

  printf( "please enter a number\n" );

  scanf( "%d", &x);

  x = abs( x );

  printf( "the absolute value of the number is %d\n", x);

  return 0;
}
