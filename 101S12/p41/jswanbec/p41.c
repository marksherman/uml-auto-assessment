/***********************************************************************/
/* Programmer: Jimmy Swanbeck                                          */
/*                                                                     */
/* Program 41: Malloc up Space for a 1-Dimensional Array of n Integers */
/*                                                                     */
/* Approximate completion time: 20 minutes                             */
/***********************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc , char *argv[] )
{
  int n;
  int i;
  int total = 0;
  printf( "Input a value for the number of integers: " );
  scanf( "%d" , &n );
  int* stuff = ( int* ) malloc( n * sizeof( int ));
  for( i = 0 ; i < n ; i++ )
    {
      printf( "Input a value for integer %d: " , i + 1 );
      scanf( "%d" , &stuff[i] );
    }
  for( i = 0 ; i < n ; i++ )
    {
      total += stuff[i];
    }
  free( stuff );
  printf( "Sum of all integers is: %d\n" , total );
  return 0;
}
