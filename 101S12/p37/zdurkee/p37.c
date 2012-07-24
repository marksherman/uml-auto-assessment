/************************************************/
/*  Programmer: Zachary Durkee                  */
/*                                              */
/*  Program 37: Digit Sum (again)               */
/*                                              */
/*  Approximate completion time: 20 minutes     */
/************************************************/

#include <stdio.h>

int digitsum( int digit );

int main( int argc, char *argv[] ){

  int sum, digit;

  FILE *fin;

  fin = fopen( argv[1], "r" );

  while( fscanf( fin, "%d", &digit ) != EOF ){

    sum = digitsum( digit );

    printf( "%d ", sum );

  }

  printf( "\n" );

  fclose( fin );

  return 0;

}

int digitsum( int digit ){

  int sum=0;

  while( digit > 0 ){

    sum += digit % 10;

    digit = digit / 10;

  }

  return sum;

}
