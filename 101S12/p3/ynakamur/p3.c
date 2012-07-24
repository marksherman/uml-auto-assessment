/*************************************************************/
/* Programmer: Yasutoshi Nakamura                            */
/*                                                           */
/* Program 3: Sum of Two Values                              */
/*                                                           */
/* Approximate completion time: 15 minutes                   */
/*************************************************************/

#include <stdio.h>

int main ( int argc, char *argv[] ) {

  int number1, number2, sum;

  printf("Please type in 2 numbers that you want to add together.\n");

  scanf( "%d %d", &number1, &number2 );

  sum = number1 + number2;

  printf( "The sum of %d and %d is equal to %d.\n", number1, number2, sum );

  return 0;

}
