/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 23: fgetc and toupper             */
/*                                           */
/* Approximate completion time: 20 minutes   */
/*********************************************/

#include <stdio.h>
#include <ctype.h>

int main( int argc, char *argv[] )
{
  char x;
  FILE *fin;
  fin=fopen( "testdata23","r" );
  while( x != EOF )
    {
      x = fgetc( fin );
      x = toupper ( x );
      if (x != EOF)
      putchar ( x );
    }
  fclose( fin );
  return 0;
}
