/***************************************/ 
/* Programmer: James DeFilippo         */ 
/* Programm 22: Sum of Bunch           */ 
/* Approximate Time: 15 minutes        */ 
/***************************************/ 

#include <stdio.h>
int main ( int argc, char* argv[] ) 
{
  FILE *fin; 
  fin = fopen( "testdata22", "r" ); 
  
  /* initialize loop variables */ 
  int nextint = 0; 
  int sum = 0; 
  
  while ( fscanf( fin, "%d", &nextint ) != EOF ) { /* if -1 is inputed in testdata, fscanf will read as non-EOF */ 
    /* increment by whatever the next integer in testdata22 is */ 
    sum = sum + x; 
  }
  printf( "%d", sum ); 
  printf( "\n" ); 
  fclose ( fin ); 
  return 0; 
}
