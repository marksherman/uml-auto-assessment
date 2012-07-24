/***********************************************************************/
/* Programmer: Theodore Dimitriou                                      */
/* Program 41: Malloc up Space for a 1-Dimensional Array of n integers */
/* Approximate completion time: 1:30 hours                             */
/***********************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char* argv[] ){
  int n = 0, i = 0, sum = 0;
  int* nums;
  
  printf( "Enter how many values you will enter:\n" );
  scanf( "%d", &n );
  nums = (int*) malloc( n * sizeof( int ) );
  printf( "Now enter the values:\n" );

  if( nums == NULL ){
    printf( "Error allocating memory! Try again.\n" );
    return 1;
  }
  
  while( i < n ){
    scanf( "%d", &nums[i] );
    i++;
  }
  
  for( i = 0; i < n; i++ )
    sum += nums[i];
  
  printf( "The sum of the values you entered is: %d\n", sum );

  free( nums );

  return 0;
}
