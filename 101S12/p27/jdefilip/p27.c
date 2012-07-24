/***************************************/ 
/* Programmer: James W. DeFilippo      */ 
/* Program 27: Reverse                 */ 
/* Approximate Time: 15 minutes        */ 
/***************************************/

#include <stdio.h>
int main ( int argc, char* argv[] ) 
{
  int i; 
  int a[ 10 ]; 
  printf( "Hi! Please enter ten integers.\n" ); 
  for ( i=0; i<=9; ++i ) 
    scanf( "%d", &a[i] ); 
  for ( i=9; i>=0; --i ) 
    printf( "%d ", a[i] ); 
  printf( "\n" ); 
  return 0; 
}
