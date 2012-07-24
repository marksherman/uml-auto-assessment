/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 10:   Sum of Twenty                          */
/* Time:         5 minutes                              */
/********************************************************/

#include <stdio.h>

int main () {

  int x ;

  int i, sum = 0 ;

  FILE * fin ;

  fin = fopen ( "testdata10", "r" ) ;

  for ( i = 0 ; i < 20 ; i++ ) {

    fscanf ( fin, "%d", &x ) ;

    sum = sum + x ;

  }

  printf ( "\nThe sum of the values in the file is %d.\n\n", sum) ;

  fclose ( fin ) ;

  return 0 ;

}
