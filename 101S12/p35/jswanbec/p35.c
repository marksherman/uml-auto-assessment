/*******************************************************/
/* Programmer: Jimmy Swanbeck                          */
/*                                                     */
/* Program 35: Passing a Two Dimensional Array         */
/*                                                     */
/* Approximate completion time: 35 minutes             */
/*******************************************************/

#include <stdio.h>

int sum( );

int main( int argc , char *argv[] )
{
  int i , j , n = 0;
  int set[3][3];
  for( i = 0 ; i < 3 ; i++ )
    for( j = 0 ; j < 3 ; j++ )
    {
      n += 1;
      printf( "Enter and integer for value %d: " , n );
      scanf( "%d" , &set[i][j] );
    }
  n = sum( set );
  printf( "The sum of the integers is: %d\n" , n );
  return 0;
}

int sum( int set[3][3] )
{
  int a , b , c = 0;
  for( a = 0 ; a < 3 ; a++ )
    for( b = 0 ; b < 3 ; b++ )
      {
	c += set[a][b];
      }
  return c;
}
