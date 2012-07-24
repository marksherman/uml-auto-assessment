/*******************************/
/* Programmer: David Hoyt      */
/* Program: Non-recursive fact */
/* Time: 20min                 */

#include <stdio.h> 
#include <stdlib.h>

int fact( int y );

int main(){

  int x;

  printf( "Enter a number:" );

  scanf( "%d", &x );

  if( x==0 )

    printf( "The factorial of %d is: 1\n", x );

else

  printf( "The factorial of %d is %d\n", x, fact( x ));

  return 0;

}

int fact( int y ){

  int i;

  for( i=(y-1); i>0; i-- ){

    y = y * i;

  }

  return y;

}
