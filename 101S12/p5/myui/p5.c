/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Bigger than 100?                 */
/*                                            */
/* Approximate completion time: 5 minutes     */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int num = 0;
  
  printf( "Enter a number: " );
  scanf( "%d", &num );

  if ( num > 100 )
    printf( "The number is bigger than 100.\n" );
  else
    printf( "The number is not bigger than 100.\n" );

  return 0;
}
