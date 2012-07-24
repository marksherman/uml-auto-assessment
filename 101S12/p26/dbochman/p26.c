/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 26:   One Dimensional Array                  */
/* Time:         6 minutes                              */
/********************************************************/
#include <stdio.h>
int main ( int argc, char *argv[] ) {
  int A[15] ;
  int i, j ;
  FILE *fin = fopen ( "testdata26", "r" ) ;
  printf ( "\n" ) ; 
  for ( i = 0 ; i < 15 ; i++ ) {
    fscanf ( fin, "%d", &A[i] ) ;
  }
  for ( j = 14 ; j >= 0 ; j-- ) {
    printf ( "%d ", A[j] ) ;
  }
  printf ( "\n\n" ) ;
  fclose ( fin ) ;  
  return 0 ;
}
