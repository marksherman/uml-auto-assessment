/********************************/
/* Author: James DeFilippo      */
/* Title : Equal to Zero?       */
/* Approximate Time: 10 minutes */
/********************************/

#include <stdio.h>
int main ( int argc, char *argv[] )
{
  int x; /* declare some integer x such that an address is initialized in memory for scanf to read into */ 
  printf( "Hi! Please enter a number. \n" ); /* prompts the user for a meaningful value */ 
  scanf( "%d", &x ); /* reads from standard input for some decimal value to put in the address of integer x */ 
  if ( x == 0 ) /* first conditional expression */ 
    printf( "The number is equal to zero.\n" ); 
  else /* fall back from conditional */ 
    printf( "The number is not equal to zero.\n" ); 
  return 0; 
}
