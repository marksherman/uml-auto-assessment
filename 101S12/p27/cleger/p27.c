/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Reverse                           */
/*                                              */
/*     Time to Completion: 25 min               */
/*                                              */
/************************************************/


#include<stdio.h>

int main( int argc, char *argv[] ) {

  int i;

  int num[15];

  printf( "Enter 15 numbers seperated by spaces:" );
  
  for( i = 0 ; i < 15 ; i++ ) {

    scanf( "%d" , &num[i] );
  }

  printf( "Backwards integers:" );

  for( i = 14 ; i >= 0 ; i-- ) {

    printf( "%d " , num[i] );

  }

  putchar( '\n' );

  return 0;

}
