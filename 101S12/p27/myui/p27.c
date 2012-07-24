/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Reverse                          */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int i, data[9];

  printf( "Enter 10 integers: " );
  
  for ( i = 0; i < 10; i++ ){
    scanf( "%d", &data[i] );
  }

  for ( i = 9; i >= 0; i--) {
    printf( "%d ", data[i] );
  }

  printf( "\n" );

  return 0;
}
