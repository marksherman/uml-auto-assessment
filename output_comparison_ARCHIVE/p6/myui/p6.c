/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Equal to Zero                    */
/*                                            */
/* Approximate completion time: 5 minutes     */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int num = 0;

  printf( "Enter a single integer: " );
  scanf( "%d", &num );

  if ( num == 0 )
    printf( "The number is equal to zero.\n" );
  else
    printf( "The number is not equal to zero.\n" );

  return 0;
}
