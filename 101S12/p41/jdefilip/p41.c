/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Malloc Up Space              */ 
/* Approximate Time: 30 minutes        */ 
/***************************************/ 

#include <stdio.h>
#include <stdlib.h>
int main ( int argc, char* argv[] ) 
{
  int* x;  
  int i;
  int sum = 0; 
  int n; 
  printf( "Please enter the size of the array.\n" ); 
  scanf( "%d", &n ); 
  x = ( int* ) malloc ( n * sizeof(int) ); 
  for ( i = 0; i < n; i++) 
    scanf("%d", &x[i]); 
  for ( i = 0; i < n; i++) 
    sum = x[i] + sum; 
  printf( "Sum of values in array is %d.\n" , sum ); 
  free ( x ); 
  return 0; 
}
