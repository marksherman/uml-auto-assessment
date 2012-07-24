/***************************************/ 
/* Programmer: James W. DeFilippo      */ 
/* Program 26: One Dimensional Array   */ 
/* Approximate Time: 15 minutes        */ 
/***************************************/


#include <stdio.h>
int main ( int argc, char* argv[] ) 
{
  int i;
  int a[ 15 ];  /* declare size of an array */ 
  FILE* fin; /* declare fin a pointer to file */ 
  fin = fopen( "testdata26", "r" ); 
  for ( i=0; i <= 14; i++ ) 
    fscanf( fin, "%d", &a[i] );  
  for ( i=14; i >= 0; i-- )
      printf( "%d\t", a[i] );  
  printf( "\n" ); 
  return 0; 
}
