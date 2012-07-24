/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 23: fgetc and toupper                                */
/*                                                              */
/* Approximate completion time: 15 minutes                      */
/****************************************************************/

#include <stdio.h>
#include <ctype.h>

int main( int argc, char *argv[] ) {

  char letter;
  FILE* fin;

  fin = fopen( "testdata23", "r" );

  while( fscanf( fin, "%c", &letter ) != EOF ) {
    fgetc( fin );
    letter = toupper( letter );
    putchar( letter );
    printf( "\n" );
  }

  fclose( fin );

  return 0;

}
