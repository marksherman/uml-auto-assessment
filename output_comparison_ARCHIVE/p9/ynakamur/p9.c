/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 9: Using a for Loop                                  */
/*                                                              */
/* Approximate comletion time: 10 minutes                       */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int value, i;
  FILE* fin;

  fin = fopen( "testdata9", "r" );

  for( i = 0; i < 5; i++ ) {
    fscanf( fin, "%d", &value );
    printf( "%d\n", value );
  }

  fclose( fin );

  return 0;

}
