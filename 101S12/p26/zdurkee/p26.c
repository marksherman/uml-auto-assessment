/******************************************************/
/*  Programmer: Zachary Durkee                        */
/*                                                    */
/*  Program 26: One Dimensional Array                 */
/*                                                    */
/*  Approximate completion time: 20 minutes           */
/******************************************************/


#include <stdio.h>

int main( int argc, char *argv[] ){

  int array[15], i;

  FILE *fin;

  fin= fopen("testdata26", "r" );

  for ( i=1; i<=15; i++ ){

    fscanf( fin, "%d", &array[i] );

  }

  for( i=15; i>0; i-- ){

    printf( "%d", array[i] );

    printf( " " );

  }

  printf( "\n" );

  return 0;

}
