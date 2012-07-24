/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 20:   Scanf Returns What?                    */
/* Time:         5 minutes                              */
/********************************************************/
#include <stdio.h>
int main ( int argc, char *argv[] ) {
  int x ;
  FILE *fin = fopen ( "testdata21", "r" ) ; 
  printf ( "\n" ) ; 
  while ( fscanf ( fin, "%d", &x ) !=EOF )
    printf ( "%d ", x ) ;
  fclose ( fin ) ;
  printf ( "\n\n" ) ; 
  return 0 ;
}
