/*************************************************************/
/* Programmer: Yasutoshi Nakamura                            */
/*                                                           */
/* Program 5: Bigger than 100?                               */
/*                                                           */
/* Approximate completion time: 15 minutes                   */
/*************************************************************/

#include <stdio.h>

int main ( int argc, char *argv[] ) {

  int number;

  printf( "Please enter a number.\n" );

  scanf( "%d", &number );

  if( number > 100 ) {
    printf( "The number is bigger than 100.\n" );
  }

  else {
    printf( "The number is not bigger than 100.\n" );
  }

  return 0;

}
