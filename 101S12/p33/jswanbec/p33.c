/*******************************************************/
/* Programmer: Jimmy Swanbeck                          */
/*                                                     */
/* Program 32: Recursive Factorial                     */
/*                                                     */
/* Approximate completion time:  6 minutes             */
/*******************************************************/

#include <stdio.h>

int fact( );

int main( int argc , char *argv[] )
{
  int x;
  int z;
  printf("Input a non-negative integer: ");
  scanf("%d" , &x );
  z = fact( x );
  printf("%d! = %d\n" , x , z );
  return 0;
}

int fact( int x )
{
  if( x <= 1 )
    return 1;
  else
    return ( x * ( fact( x - 1 ) ) );
}
