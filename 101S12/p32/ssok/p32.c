/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 32: Non recursive Factorial     */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int fact( int n );
int main(int argc, char* argv[])
{
  int x, sum;

  printf( "Please enter a positive integer:\n" );
  scanf( "%d", &x );
  sum = fact( x );
  printf( "The factorial of %d is %d\n", x, sum );

  return 0;
}
int fact( int n )
{

  int sum = 1;
  int i;
  
  for( i=1; i<=n; i++ ){
    sum *= i;
}
 
  return sum;
}
