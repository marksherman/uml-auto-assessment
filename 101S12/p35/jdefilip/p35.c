/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Passing a 2D Array           */ 
/* Approximate Time: 30 minutes        */ 
/***************************************/ 

#include <stdio.h>
int sum_function ( int y[][3] ); /* note: the first parameter in the array is left blank by convention */ 
int main ( int argc, char* argv[] ) 
{
  int i; 
  int j;
  int sum = 0; /* sum may have junk data */ 
  int x[ 3 ][ 3 ]; 
  printf( "Hi! Please enter 9 values to fill the 3x3 array.\n" ); 
  for ( i = 0; i < 3; i++ ) {
    for ( j = 0; j < 3; j++ ) 
      scanf( "%d", &x[i][j] ); 
  }

  sum = sum_function( x );
  printf( "sum of values: %d\n", sum ); 
  return 0; 
}

int sum_function ( int y[][3] ) {
  int sum = 0; /* this is important since sum may have junk data from previous initialization */ 
  int i; 
  int j; 
  for ( i = 0; i < 3; i++ ) {
    for ( j = 0; j < 3; j++ ) 
      sum = sum + y[i][j];  
  }
    return sum; 
 }
  
 
