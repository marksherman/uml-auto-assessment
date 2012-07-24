/***********************************************************/
/* Programmer: Kevin Southwick                             */
/*                                                         */
/* Program 42: Malloc up Space for a Two-Dimensional Array */
/*                                                         */
/* Approximate completion time: 45  minutes                */
/***********************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc , char* argv[] ) {

  int r, c, i, j, k, sum = 0 ;
  int* ptr;

  printf( "Type in how many rows you want, then columns. \n" );

  scanf( "%d %d" , &r , &c );

  ptr = ( int* ) malloc( r * c * sizeof( int ) ) ;

  printf( "type in integers, going across the rows, and going down. \n" );

  for( i = 0 ; i < r ; i++ )
    for( j = 0 ; j < c ; j++ )
      scanf( "%d" , &ptr[ i*c + j ] );

  printf( "which row would you like summed? \n" );

  scanf( "%d" , &k );

  for( i = 0 ; i < c ; i++ )
    sum += ptr[ k*c + i] ;
 
  printf( "The sum is: %d \n Which column would you like summed? \n" , sum );

  scanf( "%d" , &k );
  
  sum = 0;

  for( i = 0 ; i < r ; i++ )
    sum += ptr[ i*c + k ];

  printf( "The sum is: %d \n" , sum );

  sum = 0;
  
  for( i = 0 ; i < r ; i++ )
    for( j = 0 ; j < c ; j++ )
      sum += ptr[ i*c + j ];

  printf( "The sum of all elements is: %d \n" , sum );

  free( ptr );

 return 0;

}
