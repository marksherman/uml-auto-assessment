/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 26: One Dimensional Array                            */
/*                                                              */
/* Approximate completion time: 10 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int order[15], i;

  FILE* fin;

  fin = fopen( "testdata26", "r" );

  for( i = 0; i < 15; i++ ) {
    fscanf( fin, "%d", &order[i] );
  }

  for( i = 14; i >= 0; i-- ) {
    printf( "%d\n", order[i] );
  }

  fclose( fin );

  return 0;

}
