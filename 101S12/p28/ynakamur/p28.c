/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 28: Digit Sum                                        */
/*                                                              */
/* Approximate completion time: 20 minutes                      */
/****************************************************************/

#include <stdio.h>

int digitsum( int input );
int main( int argc, char *argv[] ) {

  int number, sum;
  FILE* fin;

  fin = fopen( argv[1], "r" );

  fscanf( fin, "%d", &number );

  sum = digitsum( number );

  printf( "\n%d\n\n", sum );

  fclose( fin );

  return 0;

}


int digitsum( int input ) {

  int x, sum;

  if( input == 0 ) {
    return 0;
  }

  else {
    x = input % 10;
    sum = ( input / 10 ) + x;
    return sum;
  }

}
