/**********************************************/
/*  Programmer: Zachary Durkee                */
/*                                            */
/*  Program 38: Recursive Digit Sum           */
/*                                            */
/*  Approximate completion time: 1 hour       */
/**********************************************/

#include <stdio.h>

int digitsum( int digit, int sum);

int main( int argc, char *argv[] ){

  int sum, digit;

  FILE *fin;

  fin = fopen( argv[1], "r" );

  while( fscanf( fin, "%d", &digit ) != EOF ){

    sum = 0;

    sum = digitsum( digit, sum );

    printf( "%d ", sum );

  }

  printf( "\n" );

  fclose( fin );

  return 0;

}

int digitsum( int digit, int sum ){

  if( digit <= 0 )

    return sum;

  else{

    sum += digit % 10;

    return digitsum( digit / 10, sum );

  }

}
