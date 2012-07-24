/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Using a for loop                 */
/*                                            */
/* Approximate completion time: 5 minutes     */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int num, i;
  FILE* testfile;
  
  testfile = fopen( "testdata9", "r" );
  
  for ( i = 0; i <= 5; i++){
    fscanf( testfile, "%d", &num );
    printf( "%d ", num );
  }
  
  printf( "\n" );

  return 0;
}
