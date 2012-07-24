/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : The fscanf Function              */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int num;
  FILE *file;

  file = fopen( "testdata4", "r" );
  fscanf( file, "%d", &num );
  fclose( file );
  
  printf( "The number is %d.\n", num );
  
  return 0;
}
