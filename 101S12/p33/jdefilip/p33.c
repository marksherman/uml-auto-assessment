/***************************************/ 
/* Author: James DeFilippo             */ 
/* Programm 33: Recursive Factorial    */ 
/* Approximate Time: 15 minutes        */ 
/***************************************/ 

#include <stdio.h>
int fact ( int a ); 
int main ( int argc, char* argv[] ) 
{
  int factorial; 
  int N; 
  printf( "Please enter a nonnegative integer.\n" ); 
  scanf( "%d", &N ); 
  while ( N < 0 ) {
    printf( "Please enter a nonnegative integer.\n" ); 
    scanf( "%d", &N );
  }  
  factorial = fact ( N ); 
  printf( "The factorial of %d is %d.\n", N, factorial ); 
  return 0; 

}

int fact ( int a )
{ 
  if ( a <= 1 ) /* i.e. when we come to the 0! case */  
    return 1; 
  else 
    return a * fact ( a - 1 ); 
} 
