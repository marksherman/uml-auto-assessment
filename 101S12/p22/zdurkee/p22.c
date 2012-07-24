/******************************************************/
/*  Programmer: Zachary Durkee                        */
/*                                                    */
/*  Program 22: Sum of a Bunch                        */
/*                                                    */
/*  Approximate completion time: 5 minutes            */
/******************************************************/


#include <stdio.h>

int main( int argc, char *argv[] ){
 
  int x;

  int sum = 0;

  FILE *fin;

  fin = fopen( "testdata22", "r" );

  while ( fscanf( fin, "%d", &x ) != EOF ){

    sum = sum + x;

  }

  printf( "%d\n", sum );

  fclose( fin );

  return 0;

}
