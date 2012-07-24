/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p32: Non-Recursive Factorial   */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int factorial( int n );

int main(int argc, char* argv[])
{

  int x, sum;
 
  printf( "Please enter a positive integer:\n" );
  scanf( "%d", &x );

  sum = factorial( x );

  printf( "The factorial is: %d", sum );
  printf( "\n" );

  return 0;
}
int factorial( int n ){

  int sum = 1;
  int i;

  for( i=1; i<=n; i++ ){
    sum *= i;


  }

  return sum;
}






