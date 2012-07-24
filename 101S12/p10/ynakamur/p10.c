/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 10: Sum of Twenty                                    */
/*                                                              */
/* Approximate completion time: 20 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int value, total, i;
  FILE* fin;

  fin = fopen( "testdata10", "r" );

  total = 0;

  for( i = 0; i < 20; i++ ) {
    fscanf( fin, "%d", &value );
    total = total + value;
  }

  printf( "The sum of all of the integers is equal to: %d\n", total );

  return 0;

}
