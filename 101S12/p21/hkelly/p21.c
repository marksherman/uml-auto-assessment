/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 21: scanf returns what?                  */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  int x = 0;
  FILE *fin;
  fin = fopen( "testdata21", "r" );

  printf( "\n" );

  while( fscanf( fin, "%d", &x ) != EOF ){
    printf( "%d\n", x );
  }

  fclose (fin);

  return 0;
}
