/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 27:   Reverse                                */
/* Time:         10 minutes                             */
/********************************************************/
#include <stdio.h>
int main ( int argc, char *argv[] ) {
  int x[10], i ;
  printf ( "\nPlease enter 10 intergers: " ) ;
  for ( i = 0 ; i < 10 ; i++ ) {
    scanf ( "%d", &x[i] ) ; 
  } 
  printf ( "\n" ) ;
  for ( i = 9 ; i >= 0 ; i-- ) 
    printf ( "%d ", x[i] ) ; 
  printf ( "\n\n" ) ;   
  return 0 ;
}
