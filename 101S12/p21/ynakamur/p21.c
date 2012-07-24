/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 21: Scanf returns what?                              */
/*                                                              */
/* Approximate completion time: 10 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int value;

  FILE* fin;

  fin = fopen( "testdata21", "r" );

  while( fscanf( fin, "%d", &value ) != EOF ) {
    printf( "%d\n", value );
  }
  
  fclose( fin );

  return 0;

}
