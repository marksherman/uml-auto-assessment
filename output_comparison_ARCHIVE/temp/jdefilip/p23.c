/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Fgetc and Toupper            */ 
/* Approximate Time: 15 minutes        */ 
/***************************************/ 

#include <stdio.h>
#include <ctype.h> /* toupper is contained here */ 
int main ( int argc, char* argv[] ) 
{
  int c; 
  FILE* fin; 
  fin = fopen( "testdata23", "r" ); 
  while ( ( c = fgetc( fin ) ) != EOF ) {
    putchar ( toupper ( c ) ); /* print only characters to which the toupper function has been applied (only uppercase letters in the ASCII system */ 
  }
  fclose ( fin ); 
  return 0; 
}
