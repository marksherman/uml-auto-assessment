/************************************/ 
/* Programmer: James W. DeFilippo   */ 
/* Program 28: Digit Sum            */ 
/* Approximate Time: 35 minutes     */ 
/************************************/ 




#include <stdio.h>
 
int digitsum( int x );
 
/* these are declared as global variables for use in both functions */ 
int nextint; 
int rem;
int sum = 0;
 
int main( int argc, char *argv[] ){
  FILE *fin;
  fin = fopen( argv[ 1 ], "r" );
  while( fscanf( fin, "%d", &nextint ) != EOF ) {
    printf( "%d ", digitsum( nextint ) );
    sum = 0; /* reset sum for each next integer that is read in */ 
  }
  printf( "\n" );  
  fclose( fin );
  return 0;
}
 
int digitsum( int nextint ) {
  while( nextint > 0 ) { /* otherwise, sum would always be zero (after a few recursions)! */ 
    rem = nextint % 10;
    nextint = nextint / 10;
    sum = sum + rem;
   }
 return sum;
}
