/*********************************************************/
/*  Programmer: Zachary Durkee                           */
/*                                                       */
/*  Program 8: One Horizontal Line of Asterisks          */
/*                                                       */
/*  Approximate completion time: 30 minutes              */
/*********************************************************/

#include<stdio.h>

int main( int argc, char *argv[] )

{

  int i;

  int x;

  FILE *fin;

  fin=fopen("testdata8", "r");

  fscanf( fin, "%d", &x);

  for ( i=1; i <= x; i++ ) {

    printf( "*" );

  }

  printf("\n");

  fclose(fin);

  return 0;

}
