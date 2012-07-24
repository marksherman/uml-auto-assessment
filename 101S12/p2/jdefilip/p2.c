/********************************/
/* Author: James DeFilippo      */
/* Title : The Scanf Function   */
/* Approximate Time: 5 minutes  */
/********************************/

#include <stdio.h>
int main ( int argc, char *argv[] )
{
  int x; /* create a physical address for input to be directed to */
  printf( "Please enter a value.\t" ); /* user-friendly prompt */ 
  scanf( "%d", &x ); /*  allows for input from keyboard */ 
  printf( "You typed %d as the value.\n", x ); /*  print standard input to standard output */ 
  return 0;
}
