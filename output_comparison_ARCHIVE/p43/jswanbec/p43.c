/******* *****************************************/
/* Programmer: Jimmy Swanbeck                    */
/*                                               */
/* Program 43: Square Deal                       */
/*                                               */
/* Approximate completion time: A very long time */
/*************************************************/

#include <stdio.h>
#include <stdlib.h>

int N;
int square[][];

int prime( );

void right( );

void up( );

void left( );

void down( );

int main( int argc , char *argv[] )
{
  x = ( N / 2 ) + 1;
  y = x;
  int initial_value;
  printf( "Input a value for the size of the square array: " );
  scanf( "%d" , &N );
  printf( "Input a number for the initial value: " );
  scanf( "%d" , &initial_value );
  square = ( int* ) malloc( N * N * sizeof( int ));
  square[x][y] = initial_value;
  for( i = x + 1 ; i < N - 1 ; i++ )
    {
      right( i );
      up( i );
      left( i );
      down( i );
    }
  free( square );
  return 0;
}

int prime( int x )
{
  for( i = 2 ; i < 170 ; i++ )
    {
      if(( x % i ) == 0 )
	return 1;
    }
  else
    return 0;
}

void right( );


void up( );


void left( );


void down( );

/* I worked for a really long time on this project but I never quite figured out how to do it. I'm submitting what I have just so you can see how far I got. I at least got the prime number detector worked out. */
