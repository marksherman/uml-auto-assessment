/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Non-Recursive Factorial      */ 
/* Approximate Time: 30 minutes        */ 
/***************************************/ 

#include <stdio.h>
int main ( int argc, char* argv[] ) 
{
  int N = 0; 
   
  int i;
  
  int fact; 
  
  printf( "Please enter a nonnegative integer.\n" );  
  
  scanf( "%d", &N ); 
 
  while ( N < 0 ) { 
      printf( "Please enter a nonnegative integer." ); 
      scanf( "%d", &N ); 
  }
  
  fact = N; 
  
  for ( i = 1; i < N; i++ ) {
      fact = fact * ( N - i );
  }
 
  if ( N == 0 ) 
      fact = 1; 
  printf( "The factorial of this integer is %d.\n", fact ); 

  return 0; 

}
