/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 23:   fgetc and toupper                      */
/* Time:         5 minutes                              */
/********************************************************/
#include <stdio.h>
#include <ctype.h>
int main ( int argc, char *argv[] ) {
  char x ;
  FILE *fin = fopen ( "testdata23", "r" ) ;
  printf ( "\n" ) ;
  while ( ( x = fgetc ( fin ) ) != EOF )
    putchar ( toupper(x) ) ;
  fclose ( fin ) ;
  printf ( "\n\n" ) ;  
  return 0 ;
}
