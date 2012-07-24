/**************************************************/
/*  Programmer: Zachary Durkee                    */
/*                                                */
/*  Program 28: Digit Sum                         */
/*                                                */
/*  Approximate completion time: 2 hr.            */
/**************************************************/

/*** Note:  Mike helped a lot. Hinted for modulus use in function.  ***/


#include <stdio.h>

int digitsum( int digit );

int main( int argc, char * argv[] ){

  int sum, digit;

  FILE *fin;

  fin = fopen( argv[1], "r" );

  fscanf( fin, "%d", &digit );

  sum = digitsum( digit );

  printf( "%d\n", sum );

  fclose( fin );

  return 0;

}

int digitsum( int digit ){

  int i, x, sum=0;

  for( i=0; x != 0; i++ ){

    x = digit % 10;

    digit = digit / 10;
    
    sum = sum + x;

  }

  return sum;

}

