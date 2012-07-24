/****************************************/
/* Programmer: Kevin Southwick          */
/*                                      */
/* Program 43: Square Deal              */
/*                                      */
/* Approximate completion time: 5 hours */
/****************************************/

#include <stdio.h>
#include <stdlib.h>

int prime( void );
void move_up( int* ptr );
void move_down( int* ptr );
void move_left( int* ptr );
void move_right( int* ptr );

int i, j, N, initial_value, repeat_val = 1;

int main( int argc , char* argv [] ) {
  
  int* ptr;

  printf( "input two positive integers, a size N, then an initial value. \n" );
  scanf( "%d %d" , &N, &initial_value );

  ptr = ( int* ) malloc( N * N * sizeof( int ) ) ;

  i =  N / 2; /* sets i and j to the center */
  j = i;

  ptr[ i*N + j ] = prime( );
  move_right( ptr ); 
  /* need to start and end with rightward movement, the end is in the loop, */
  /* so the start is outside the loop                                       */

  while( ( i <=  ( N - 1 )  ) && ( j <= ( N - 1 )  ) ){
      move_up( ptr );
      move_left( ptr );
      move_down( ptr );
      move_right( ptr );
  }

  for( i = 0 ; i < N ; i++ ){
    for( j = 0 ; j < N ; j++ )
      if( ptr[ i*N + j ] == 0 )
	printf( "*** " );
      else
	printf( "%3d " , ptr[ i*N + j ] );
    printf( "\n" );
  }
      
  free( ptr );
 
  return 0 ;
}

int prime( void ){
  /* determines if x is prime*/
  
  int k, x = initial_value;
  
  for( k = 2 ; k <= x ; k++ ){
    if( ( ( x % k ) == 0 ) && ( x != k ) ) 
      x = 0;
  }
  
  initial_value++;
  
  return x;
}

void move_right( int* ptr ){

  int k;

  for( k = 0 ; k < repeat_val ; k++ ){
    j++;
    ptr[ i*N + j ] = prime( );
  } 

  return;
}


void move_left( int* ptr ){
  
  int k;

  for( k = 0 ; k < repeat_val ; k++ ){
    j--;
    ptr[ i*N + j ] = prime( );
  }

  return;
}


void move_up( int* ptr ){

  int k;

  for( k = 0 ; k < repeat_val ; k++ ){  
    i--;
    ptr[ i*N + j ] = prime( );
  }

  repeat_val++;

  return;
}


void move_down( int* ptr ){

  int k;

  for( k = 0 ; k < repeat_val ; k++ ){
    i++;
    ptr[ i*N + j ] = prime( );
  }

  repeat_val++;

  return;
}
