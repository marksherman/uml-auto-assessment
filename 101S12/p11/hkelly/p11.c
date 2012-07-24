/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 11: The abs Function                     */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char* argv[] ){
  
  int x;

  printf("Enter a number.\n");
  scanf("%d", &x);

  printf("The absolute value of your number is: %d\n", abs(x));

  return 0;
}
