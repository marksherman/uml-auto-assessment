/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Sum of a Bunch                   */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {
  
  int numbers, total;
  FILE *scanfile;

  numbers = 0;
  total = 0;
  scanfile = fopen( "testdata22", "r" );

  while ( fscanf( scanfile, "%d", &numbers ) != EOF ) {
    total = total + numbers; 
  }

  printf( "%d\n", total );

  fclose( scanfile );

  return 0;
}
