/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 24: Find the Average                                 */
/*                                                              */
/* Approximate completion time: 10 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int numbers[4], i;

  float average = 0;

  FILE* fin;

  fin = fopen( "testdata24", "r" );

  for( i = 0; i < 4; i++ ) {
    fscanf( fin, "%d", &numbers[i] );
    average = numbers[i] + average;
  }

  average = average / 4;

  printf( "\nThe average of the 4 integers is %f\n\n", average );

  fclose( fin );

  return 0;

}
