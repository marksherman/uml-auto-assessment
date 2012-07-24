/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Sum of Twenty                    */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int num, i, total = 0;
  FILE* testfile;

  testfile = fopen( "testdata10", "r" );
  
  for ( i = 1; i <= 20; i ++ ){
    fscanf( testfile, "%d", &num );
    total = total + num;
  }

  printf( "%d\n", total );
  
  return 0;
}
