/*****************************************************/
/*  Programmer:  Zachary Durkee                      */
/*                                                   */
/*  Program 9: Using a for Loop                      */
/*                                                   */
/*  Approximate completion time: 30 minutes          */
/*****************************************************/

#include<stdio.h>

int main( int argc, char *argv[] )

{

  int i;

  int x;

  FILE* fin;

  fin= fopen("testdata9", "r");

  for( i=0; i<5; i++ ){

      fscanf( fin, "%d", &x);

      printf( "%d", x);

  }

  printf("\n");

    fclose(fin);

    return 0;

  }
