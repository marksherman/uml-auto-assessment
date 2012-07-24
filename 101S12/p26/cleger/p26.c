/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: One Dimensional Array             */
/*                                              */
/*     Time to Completion: 15 min               */
/*                                              */
/************************************************/

#include<stdio.h>

int main( int argc, char *argv[] ){


  FILE *fin;

  fin = fopen( "testdata26", "r" );

  int numbers[15];

  int i;

  for( i = 0 ; i < 15 ; i++ ) {

    fscanf( fin , "%d" , &numbers[i] );
  }


  fclose( fin );

  for( i = 14 ; i >= 0 ; i-- ) {

    printf( "%d " , numbers[i] );
  }

  putchar( '\n' );

  return(0);

}
