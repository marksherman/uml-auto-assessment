/****************************************************/
/* Programmer: Theodore Dimitriou                   */
/* Program 33: Recursive Factorial                  */
/* Approximate completion time: 10 mins             */
/****************************************************/

#include <stdio.h>

int fact( int a );

int main( int argc, char* argv[] ) {

  int a, x;
  
  printf( "Enter a single integer: " );

  x = scanf( "%d", &a );
  
  if( * &a < 0  )
    printf( "Factorial is only defined for nonnegative integers\n" );
  else
    printf( "The factorial of %d is: %d\n", a, fact( a ) );
  
  return 0;
}

int fact( int a )
{
  if( a == 0 )
    return 1;
  else
    return a * fact( a - 1 );
  /* Should never reach here */
  return 1;
}
