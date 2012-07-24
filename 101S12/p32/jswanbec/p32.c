/*******************************************************/
/* Programmer: Jimmy Swanbeck                          */
/*                                                     */
/* Program 32: Non-recursive Factorial                 */
/*                                                     */
/* Approximate completion time: 20 minutes             */
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
  int i;
  int y = 1;
  for( i = x ; i > 0 ; i--)
    {
      y *= i;
    }
  return y;
}
