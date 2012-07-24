/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 37: Digit Sum (again)              */
/*                                            */
/* Approximate completion time: 30  minutes   */
/**********************************************/

#include <stdio.h>

int digitsum( int X );

int main( int argc , char* argv[] ) {

  int x ;

  FILE *fin;

  fin = fopen( argv[1] , "r" );

  while( (fscanf( fin ,  "%d" , &x )) != EOF )

    printf( "%d " , digitsum( x ) );

  printf( "\n" );

  fclose( fin );

  return 0;

}

int digitsum( int x ){

  int sum = 0;

  while( x > 0 ){

    sum += ( x % 10 ) ;

    x = x / 10 ;

  }

  return sum;

}
