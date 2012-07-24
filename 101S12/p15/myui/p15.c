/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Solid Box of Asterisks           */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int L = 0, H = 0, i, j;

  printf( "Enter the length and the height: " );
  scanf( "%d%d", &L, &H );

  for( i = 0; i < H; i++ ){

    for( j = 0; j < L; j++ ){
      
      printf( "*" );

    }

    printf( "\n" );

  }

  return 0;
}
