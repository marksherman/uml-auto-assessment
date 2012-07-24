/********************************/
/* Author: James DeFilippo      */
/* Title : Using a for loop     */
/* Approximate Time: 10 minutes */
/********************************/

#include<stdio.h>
int main ( int argc, char *argv[] ) 
{
  int x; /* initialize a variable to store input from fscanf */ 
  int i; /* initialize an index variable for the for loop */ 
  FILE *fin; /* direct program to location of external file */
  fin = fopen ( "testdata9", "r" ); /* specify address and deal with file permissions (make readable "r") */
  for ( i = 1; i <= 5; i++ )
    {
      fscanf ( fin, "%d", &x ); /* select an expression using a given type */
      printf( "%d\n", x ); /* print said expression via standard output */
      x = 0; /* once the value of x is printed for the particular case, that value is irrelevant to the next execution of the for loop and can be       cleared from memory */ 
    }
  fclose ( fin ); /* close the file just accessed */
  return 0; 
}
