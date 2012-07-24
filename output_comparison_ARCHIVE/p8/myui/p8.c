/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : One Horizontal Ling of Asterisks */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int num, i = 0;
  FILE* testfile;

  testfile = fopen( "testdata8", "r" );
  fscanf( testfile, "%d", &num );

  for ( i = 1; i <=  num; i++ ){
    printf( "*" );
  }
  
  printf( "\n" );

  return 0;
}
