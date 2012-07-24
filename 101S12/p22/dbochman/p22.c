/********************************************************/
/* Programmer:   Dylan Bochman                         */
/* Program 22:   Sum of a Bunch                         */
/* Time:         5 minutes                              */
/********************************************************/
#include <stdio.h>
int main ( int argc, char *argv[] ) {
  int x, sum ;
  FILE *fin = fopen ( "testdata22", "r" ) ;
  while ( fscanf ( fin, "%d", &x ) != EOF )
    sum += x ;
  printf ( "\nThe sum of the numbers in the file is %d.\n\n", sum ) ;
  fclose ( fin ) ; 
  return 0 ;
}
