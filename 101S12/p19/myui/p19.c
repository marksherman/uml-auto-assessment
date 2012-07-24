/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Argv                             */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {
  
  int i;
  
  for( i = 0; i < argc; i++ ){

    printf( "%s\n", argv[i] );

  }

  return 0;
}
