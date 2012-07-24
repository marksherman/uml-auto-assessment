/**********************************************************/
/* Programmer: Yasutoshi Nakamura                         */
/*                                                        */
/* Program 2: The scanf Function                          */
/*                                                        */
/* Approximate completion time: 5 minutes                 */
/**********************************************************/

#include <stdio.h>

int main ( int argc, char *argv[] ) {

  int value;

  printf("Please type in a number:\n");

  scanf( "%d", &value );

  printf( "You typed in %d.\n", value );

  return 0;

}
