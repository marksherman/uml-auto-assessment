/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 33: Recursive Factorial         */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int fact( int n );
int main(int argc, char* argv[])
{
  int n;
  printf( "\nPlease enter a positive integer:\n" );
  scanf( "%d", &n );
  
  if ( n < 0 )
    printf("\n Must be greater than 0\n");
  else
    printf( "\nFactorial of %d is %d\n", n , fact( n ));

  return 0;
}
int fact( int n )
{

  if( n <= 1 )
    return 1;
  else
    return n* fact( n - 1);
}
