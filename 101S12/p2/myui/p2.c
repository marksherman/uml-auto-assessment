/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : The scanf Function               */
/*                                            */
/* Approximate completion time: 5 minutes     */
/**********************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int temp;
  
  printf( "Enter a number: ");
  scanf( "%d", &temp );

  printf( "You entered: ");
  printf( "%d\n", temp );

  return 0;
}
