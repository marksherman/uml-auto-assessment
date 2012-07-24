/********************************************************/
/* Programmer: Theodore Dimitriou                       */
/* Program 32: Non-recursive Factorial - Use a function!*/
/* Approximate completion time: 60 mins                 */
/********************************************************/

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
  int i, j = 1;
  
  for( i = 1; i <= a; i++ )
    j = j * i;

  return j;
}
