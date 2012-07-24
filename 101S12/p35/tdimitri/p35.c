/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 35: Passing a Two Dimensional Array   */
/* Approximate completion time: 55 mins          */
/*************************************************/

#include <stdio.h>
#include <stdlib.h>

int sum( int u[ ][3] );

int main( int argc, char* argv[] ) {
  
  int u[3][3], i, j = 0;
  
  printf( "\nEnter 9 integer values: " );
  
  for( i = 0; i < 3; i++ ){
    for( j = 0; j < 3; j++)
      scanf( "%d", &u[i][j] );
  }
  
  printf( "The sum of the values is: %d\n", sum( u ) );

  return 0;
}

int sum( int u[ ][3] )
{
  int i, SUM = 0, j;
  
  for( i = 0; i < 3; i++ ){
    for( j = 0; j < 3; j++ )
      SUM+= u[i][j];
  }
  return SUM;
}
