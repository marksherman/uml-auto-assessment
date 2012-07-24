/*****************************************/
/* Author: James DeFilippo               */
/* Title: Square Deal                    */
/* Approximate Time: 60 minutes          */
/*****************************************/

#include <stdio.h>
#include <stdlib.h>
int main ( int argc, char* argv[] )
{
  int* x; /* size of array */

  int N;
  int initial_value;
  int next_int;
  int ch;
  int cv;

  int offset = 1;
  int i;
  int j;
  int k;
  int l;



  printf( "Please enter the side length of the array followed by the initial value at the center." );
  scanf("%d %d", &N, &initial_value);
  x = ( int* ) malloc ( N * N * sizeof(int) );

  next_int = initial_value + 1;
  i = 0;
  j = 0;
  k = 0;
  l = 0;
  offset = 1;
  ch = cv = N/2;
  while (( ( ch + i ) < ( N - 1 ) ) && (( (cv + j ) < (N - 1) ))) {

    /* move to the right one ALWAYS */
    x[ (ch)*N + (cv) ] = initial_value;
    i++;
    x[ ( ch + i )*N + ( cv +j ) ] = next_int;
    next_int++;

    /* move up by offset */
    j = offset - 1;
    while ( j > (-offset) ) {
      j--;
      x[ ( ch+i )*N + ( cv + j ) ] = next_int;
      next_int++;
    }

    /* move to the left by offset */ 
    k = offset;
    while ( k > -offset) {
      k--;
      x[(ch+k)*N + (cv + j)] = next_int;
      next_int++;
    }

    /* move down by offset */ 
    l = -offset;
    while ( l < offset) {
      l++;
      x[(ch+k)*N + (cv + l)] = next_int;
      next_int++;
    }

    /* move to the right by offset */ 
    k = -offset;
    while ( k < offset ) {
      k++;
      x[(ch+k)*N + (cv + l)] = next_int;
      next_int++;
    }

    offset++;


  }

  for ( j = 0; j < N; j++) {
    for ( i = 0; i < N; i++) {
      if ( (((x[i*N + j]) % 2) && ((x[i*N+j]) != 1)) || ((x[i*N + j] == 2)))
	printf("%d    ", x[i*N + j]);
      else
	printf("***  ");
    }
    printf("\n\n");
  }

  return 0;
}
