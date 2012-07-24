/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Recursive Digit Sum              */
/*                                            */
/* Approximate completion time: 15 mins       */
/**********************************************/

#include<stdio.h>

int digitsum( int x );

int main( int argc, char *argv[] ) {
  
  int x, sum;
  FILE* scanfile;

  x = 0;
  sum = 0;

  scanfile = fopen( argv[1], "r" );

  while( fscanf( scanfile, "%d", &x ) != EOF ) {  
    sum = digitsum( x );
    printf( "%d ", sum );
  }

  printf( "\n" );

  return 0;
}

int digitsum( int x ) {

  int lastdigit, remainder;

  lastdigit = 0;
  remainder = 0;

  if( x < 9 ) 
    return x;
  else {
    lastdigit = x % 10;
    remainder = x / 10;
    return digitsum( remainder ) + lastdigit;

  }

}
