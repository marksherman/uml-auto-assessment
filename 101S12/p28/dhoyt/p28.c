/******************************/
/* Programmer: David Hoyt     */
/* Program: Digit Sum         */
/* Time: 20min                */

#include <stdio.h>
#include <stdlib.h>

int x, sum;

int dsum( int x );

int main( int argc, char* argv[] ){

  FILE* blu;

  blu = fopen( argv[1], "r" );

  int dsum( int sum ){

    while( (fscanf( blu, "%d", &x )) != EOF ){

    fscanf( blu, "%d", &x );

    sum = sum + x;

    }

    fclose( blu );

    return sum; 

  }

  printf( "%d\n", sum );

  return 0;

}
