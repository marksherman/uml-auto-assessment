/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : One Dimensional Array            */
/*                                            */
/* Approximate completion time: 5 minutes     */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int i, data[14];
  FILE *scanfile;

  scanfile = fopen ( "testdata26", "r" );

  for ( i = 0; i < 15 ; i++ ) {
    fscanf( scanfile, "%d", &data[i] );
  }
  
  for ( i = 14; i >=  0; i-- ) {
    printf ( "%d ", data[i] ) ;
  }

  printf( "\n" );

  fclose( scanfile );

  return 0;
}
