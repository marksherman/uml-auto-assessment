/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Positive, Negative, or Zero      */
/*                                            */
/* Approximate completion time: 5 minutes     */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int num = 0;

  printf( "Enter a single integer number:" );
  scanf( "%d", &num );

  if ( num > 0 )
    printf( "The number is postive.\n" );
  else if ( num < 0 )
    printf( "The number is negative.\n" );
  else 
    printf( "The number is zero.\n" );

  return 0;
}
