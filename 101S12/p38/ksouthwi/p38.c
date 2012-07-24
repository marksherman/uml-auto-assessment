/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 38: Recursive Digit Sum            */
/*                                            */
/* Approximate completion time: 10  minutes   */
/**********************************************/

#include <stdio.h>

int digitsum( int x );

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

  if( x < 10 ) /* base case, x is one digit */

    return x; /* so the digitsum of one digit is that digit */

  else
    
    return( ( x % 10 ) + digitsum( x / 10 ) );
  /* return the first digit then add the return of the recursive call,   */
  /* which is the digitsum of the number with the last digit knocked off */

}
