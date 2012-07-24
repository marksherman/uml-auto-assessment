/************************************************************/
/*  Programmer: Zachary Durkee                              */
/*                                                          */
/*  Program 21: scanf returns what?                         */
/*                                                          */
/*  Approximate completion time:  10 minutes                */
/************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ){

  int x;

  int count = 0;

  FILE *fin;

  fin = fopen( "testdata21", "r" );

  while ( fscanf( fin, "%d", &x) != EOF ){

    count++;

  }

  printf( "%d\n", count );

  fclose( fin );

  return 0;

}
