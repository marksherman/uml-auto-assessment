/************************************/ 
/* Programmer: James W. DeFilippo   */ 
/* Program 28: Digit Sum            */ 
/* Approximate Time: 35 minutes     */ 
/************************************/ 




#include <stdio.h>
 
int digitsum( int x );
 
int main( int argc, char *argv[] ){
  int nextint; 
  FILE *fin;
  fin = fopen( argv[ 1 ], "r" );
  while( fscanf( fin, "%d", &nextint ) != EOF ) {
    printf( "%d ", digitsum( nextint ) );
  }
  printf( "\n" );  
  fclose( fin );
  return 0;
}
 
int digitsum( int nextint ) {
  int sum = 0; 
  int rem; 
  while( nextint > 0 ) { /* otherwise, sum would always be zero (after a few recursions)! */ 
    rem = nextint % 10;
    nextint = nextint / 10;
    sum = sum + rem;
   }
 return sum;
}
