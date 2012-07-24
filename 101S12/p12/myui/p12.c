/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Using the squrt Function         */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>
#include<math.h>

int main( int argc, char *argv[] ) {
  
  int num;
 
  printf( "Enter a number: " );
  scanf( "%d", &num );

  printf( "The square root of %d is %lf.\n", num, sqrt( num ) );

  return 0;
}
