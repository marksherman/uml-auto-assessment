/***********************************************************/
/* Programmer: Theodore Dimitriou                          */
/* Program 43: Square Deal                                 */
/* Approximate completion time: 9 hours                    */
/***********************************************************/

#include <stdio.h>
#include <stdlib.h>

int IsPrime( int n );

int main ( int argc, char* argv[] ) {
  
  int s = 0, k = 1, N = 0, initial_value = 0, i = 0, j = 0;
  int* ptr;
  
  printf( "\nInput the array size now: " );
  scanf( "%d", &N );
  printf( "Input the initial value: " );
  scanf( "%d", &initial_value );
  
  ptr = ( int * ) malloc( N * N * sizeof( int ) );
  
  i = N / 2;
  j = N / 2;
  
  while( k < N ){
    /*Going Right*/
    for( s = 0; s < k; s++, j++, initial_value++ )
      if( IsPrime( initial_value ) == 1 )
	ptr[ i * N + j ] = initial_value;
      else
	ptr[ i * N + j ] = -1;
    /*Going Up*/
    for( s = 0; s < k; s++, i--, initial_value++ )
      if( IsPrime( initial_value ) == 1 )
	ptr[ i * N + j ] = initial_value;
      else
	ptr[ i * N + j ] = -1;
    k++;
    /*Going Left*/
    for( s = 0; s < k; s++, j--, initial_value++ )
      if( IsPrime( initial_value ) == 1 )
	ptr[ i * N + j ] = initial_value;
      else
	ptr[ i * N + j ] = -1;
    /*Going Down*/
    for( s = 0; s < k; s++, i++, initial_value++ )
      if( IsPrime( initial_value ) == 1 )
	ptr[ i * N + j ] = initial_value;
      else
	ptr[ i * N + j ] = -1;
    k++;
  }  
  
  /*Bottom left corner*/
  if( IsPrime( initial_value ) == 1 )
    ptr[ i * N + j ] = initial_value;
  else
    ptr[ i * N + j ] = -1;
  
  /*Going Right till end*/
  k = 0;
  while( k < N - 1 ){
    for( s = 0; s < k; s++, j++, initial_value++ ){
      if( IsPrime( initial_value ) == 1 )
	ptr[ i * N + j ] = initial_value;
      else
	ptr[ i * N + j ] = -1;
    }
    k++;
  }
  
  for( i = 0; i < N; i++ ){
    for( j = 0; j < N; j++){
      if( ptr[ i * N + j ] == -1 )
	printf( "***\t" );
      else
	printf( "%d\t", ptr[ i * N + j ] );
    }
    putchar( '\n' );
  }
  /* Freeing gives me trouble sometimes.
  free( ptr );
  */
  return 0;
}

int IsPrime( int n )
{
  int i = 0;
  
  for( i = 2; i <= n - 1; i++ )
    if( ( n % i ) == 0 )
      return 0;
  if( i == n )
    return 1;
  
  return 0;
}
