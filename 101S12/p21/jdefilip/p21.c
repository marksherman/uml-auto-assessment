/***************************************/ 
/* Programmer: James DeFilippo         */ 
/* Program 21: scanf returns what?     */ 
/* Approximate Time: 15 minutes        */ 
/***************************************/ 

#include <stdio.h>
int main ( int argc, char* argv[] ) 
{
  int x; 
  FILE *fin; 
  fin = fopen( "testdata21", "r" ); 
  while ( fscanf( fin, "%d", &x ) != EOF ) /* check for end-of-file, if end-of-file not reached, print integer */
	printf( "%d ", x );
  printf( "\n" ); 
  fclose ( fin ); 
  return 0; 
}
