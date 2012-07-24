/********************************************************************/
/* Programmer: Jimmy Swanbeck                                       */
/*                                                                  */
/* Program 41: Malloc up Space for a Two-Dimensional Array          */
/*                                                                  */
/* Approximate completion time: 50 minutes                          */
/********************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc , char *argv[] )
{
  int r;
  int c;
  int n;
  int i;
  int j;
  int rows = 0;
  int columns = 0;
  int total = 0;
  printf( "Input a value for the number of rows: " );
  scanf( "%d" , &r );
  printf( "Input a value for the number of columns: " );
  scanf( "%d" , &c );
  int* values = ( int* ) malloc( r * c * sizeof( int ));
  for( i = 0 ; i < r ; i++ )
    {
      for( j = 0 ; j < c ; j++ )
	{
	  printf( "Input a value for element %d: " , ( i * c ) + j );
	  scanf( "%d" , &values[ ( i * c ) + j ] );
	}
    }
  printf( "Enter a value (0 to %d) for the row of integers to be summed: " , r - 1 );
  scanf( "%d" , &n );
  for( i = 0 ; i < c ; i++ )
    {
      rows += values[ ( n * c ) + i ];
    }
  printf( "Value of integers in row %d: %d\n" , n , rows );
  printf( "Enter a value (0 to %d) for the column of integers to be summed: " , c - 1 );
  scanf( "%d" , &n );
  for( i = 0 ; i < r ; i++ )
    {
      columns += values[ n + ( i * c ) ];
    }
  printf( "Value of integers in column %d: %d\n" , n , columns );
  for( i = 0 ; i < ( r * c ) ; i++ )
    {
      total += values[i];
    }
  free( values );
  printf( "Sum of all integers is: %d\n" , total );
  return 0;
}
