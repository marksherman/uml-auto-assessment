/************************************/ 
/* Programmer: James W. DeFilippo   */ 
/* Program 38: Recursive Digit Sum  */ 
/* Approximate Time: 40 minutes     */ 
/************************************/ 


#include <stdio.h>
 
int digitsum( int nextint );
 
int main( int argc, char *argv[] ){
  int nextint; 
  FILE *fin;
  fin = fopen( argv[ 1 ], "r" );
  while( fscanf( fin, "%d", &nextint ) != EOF ) {
    printf( "sum: %d ", digitsum( nextint ) );
  }
  printf( "\n" );  
  fclose( fin );
  return 0;
}

int digitsum ( int nextint ) { 
  if ( nextint < 10 )  
    return nextint;  
  else 
    return nextint % 10 + digitsum ( nextint / 10 );  
}

 
 
 
 
 
