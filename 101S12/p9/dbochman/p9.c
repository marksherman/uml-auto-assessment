/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 9:    Using a For Loop                       */
/* Time:         2 minutes                              */
/********************************************************/

#include <stdio.h>

int main () {

  int x, i ;

  FILE * fin ;

  fin = fopen ( "testdata9", "r" ) ;

  printf ( "\n" ) ;

  for ( i = 0 ; i < 5 ; i++ ) {

    fscanf ( fin, "%d", &x ) ;

    printf ( "%d ", x ) ;

  }

  fclose ( fin ) ;

  printf ( "\n\n" ) ;

  return 0 ;

}
