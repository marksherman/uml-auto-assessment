/****************************************************/
/*  Programmer: Zachary Durkee                      */
/*                                                  */
/*  Program 24: Find the Average                    */
/*                                                  */
/*  Approximate completion time: 25 minutes         */
/****************************************************/

#include <stdio.h>

#include <stdlib.h>

int main( int argc, char *argv[] ){

  int x;

  int sum = 0;

  float avg;

  FILE *fin;

  fin = fopen( "testdata24", "r" );

  while( fscanf( fin, "%d", &x ) != EOF ){

    sum = sum + x;

  }

  avg = ( float ) sum / 4;

  printf( "%f\n", avg );

  fclose ( fin );

  return 0;

}
