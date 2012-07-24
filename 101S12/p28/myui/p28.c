/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Digit Sum                        */
/*                                            */
/* Approximate completion time: 15 minutes    */
/**********************************************/

#include<stdio.h>
#include<stdlib.h>

int digitsum( int n );

int main( int argc, char *argv[] ) {

  char *characters;
  int sum, n;

  FILE *scanfile;

  scanfile = fopen( argv[1] , "r" );

  fscanf( scanfile, "%s", &characters );

  n = atoi( characters );
  sum = digitsum( n );

  printf( "The sum is %d.\n", sum );
  
  fclose( scanfile );

  return 0;
}

int digitsum( int n ){

  int digit, sum = 0;

  while ( n > 10 ) {
    
    digit = n % 10;
    sum = sum + digit;
    n = n / 10;

  }

  return sum;
}
