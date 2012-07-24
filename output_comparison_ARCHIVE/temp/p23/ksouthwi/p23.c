/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 23: fgetc and toupper              */
/*                                            */
/* Approximate completion time: 20  minutes   */
/**********************************************/

#include <stdio.h>
#include <ctype.h>

int main( int argc , char* argv[] ) {

  char c;

  FILE *fin;

  fin = fopen( "testdata23" , "r" );

  while( (c = fgetc ( fin )) != EOF ){

    c = toupper( c );
     
    printf("%c" , c );

  }

  printf( "\n" );

  fclose( fin );

  return 0;

}
