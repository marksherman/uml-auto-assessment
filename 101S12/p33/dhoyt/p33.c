/********************************/
/* Programmer: David Hoyt       */
/* Program: Recursive Factorial */
/* Time: 15min                  */

#include <stdio.h>
#include <stdlib.h>

int fact( int y );

int main(){

  int x;

  printf( "Enter a number:" );

  scanf( "%d", &x );

  printf( "The factorial of %d is %d\n", x, fact( x ));

  return 0;

}

int fact( int y ){

  if( y<=1 )

    return 1;

  else

    return y * fact( y-1 );

} 
