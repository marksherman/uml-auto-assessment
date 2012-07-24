/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 22: Sum of a Bunch                                   */
/*                                                              */
/* Approximate completion time: 10 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int total = 0, number;

  FILE* fin;

  fin = fopen( "testdata22", "r" );

  while( fscanf( fin, "%d", &number ) != EOF ) {
    total = number + total;
  }

  printf( "\nThe sum of the numbers is equal to %d.\n\n", total );

  fclose( fin );

  return 0;

}
