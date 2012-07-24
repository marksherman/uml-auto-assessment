/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 30: Simulating Call By Reference      */
/* Approximate completion time: 25 mins          */
/*************************************************/

#include <stdio.h>
#include <stdlib.h>

void swap( int* a, int* b );

int main( int argc, char* argv[] ) {
  int x, y;
  
  x = atoi( argv[1] );
  y = atoi( argv[2] );
  
  swap( &x, &y );
  
  printf( "%d %d\n", x, y );
  
  return 0;
}

void swap( int* a, int* b )
{
  int tmp;
  tmp = *a;
  *a = *b;
  *b = tmp;
  return;
}
