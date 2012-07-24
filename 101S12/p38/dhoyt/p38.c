/**********************************/
/* Programmer: David Hoyt         */
/* Program: Recursive Digit Sum   */
/* Time: 40min                    */

#include <stdio.h>
#include <stdlib.h>

int x, sum;

int dsum( int x );

int main( int argc, char* argv[] ){

  FILE* nums;

  int x;

  nums = fopen( argv[1], "r" );

  while( (fscanf( nums, "%d", &x)) != EOF )

    printf( "%d\n", dsum( x ) );

  return 0;

}

  int dsum( int x ){

    int last;

    last = x % 10;

    x = x / 10;

    if( x==0 ){

      return last;

    }

      else{

    return dsum( (last + x) );

  }

  }
