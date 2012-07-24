/***********************************************************************/
/* Programmer: Kevin Southwick                                         */
/*                                                                     */
/* Program 41: Malloc up Space for a 1-Dimensional Array of n integers */
/*                                                                     */
/* Approximate completion time: 30  minutes                            */
/***********************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc , char* argv[] ) {

  int n, i , sum = 0 ;
  int* ptr;

  printf( "How many integers will you type in, disregarding the first? \n" );

  scanf( "%d" , &n );

  ptr = ( int* ) malloc( n * sizeof( int ) ) ;

  for( i = 0 ; i < n ; i++ )
    
    scanf( "%d" , &ptr[i] );

  for( i = 0 ; i < n ; i++ )

    sum += ptr[i] ;
 
  printf( "The sum is: %d \n" , sum );

  free( ptr );

 return 0;

}
