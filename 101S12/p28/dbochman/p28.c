/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 28:   Digit Sum                              */
/* Time:         45 minutes                             */
/********************************************************/
#include <stdio.h>
int digitsum ( x )
 {
  int digit, sum = 0 ;
  while ( x > 0 ) {
    digit = x % 10 ;
    x = x / 10 ;
    sum = sum + digit ;
  }
  return sum ;
}
int main ( int argc, char *argv[] ) {
  int x ;
  FILE *fin = fopen ( argv[1], "r" ) ;
  printf ( "\n" ) ;
  while ( fscanf ( fin, "%d", &x ) != EOF ) 
    printf ( "%d", digitsum (x) ) ;
  fclose ( fin ) ; 
  printf ( "\n\n" ) ;  
  return 0 ;
}
