/********************************/
/* Author: James DeFilippo      */
/* Title : The fscanf function  */
/* Approximate Time: 15 minutes */
/********************************/

#include <stdio.h>
int main ( int argc, char *argv[] ) 
{
  int x; /* creates a variable to transfer between files */ 
  FILE *fin; /* direct program to location of external file */ 
  fin = fopen ( "testdata4", "r" ); /* specify address and deal with file permissions (make readable "r") */ 
  fscanf ( fin, "%d", &x ); /* select an expression using a given type */ 
  printf("%d\n", x); /* print said expression via standard output */ 
  fclose ( fin ); /* close the file just accessed */ 
  return 0; 
}
