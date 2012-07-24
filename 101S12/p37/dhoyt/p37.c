/******************************/
/* Programmer: David Hoyt     */
/* Program: Digit Sum         */
/* Time: 40min                */

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

    int last, sum=0;

    while( x>0 ){

    last = x % 10;

    sum = sum + last;

    x = x / 10;

  }

    return sum; 

  }  
