/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p33: Recursive Factorial       */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int factorial( int n );

int main(int argc, char* argv[])
{

  int y, sum;

  printf( "Please enter an integer:\n" );
  scanf( "%d", &y );

  sum = factorial( y );

  printf( "The factorial is: %d", sum );
  printf( "\n" );

  return 0;
}
int factorial( int n ){

  if( n==1 )
    return 1;
  else
    return n * factorial (n-1);

}
