/******************/
/*  Betty Makovoz */
/*Count Characters*/
/*   15 minutes   */
/******************/
 
#include <stdio.h>

int main( int argc, char *argv[] ) {

  int x;
  x=0;
  printf( "Type something:\n " ) ;
  while( getchar() != EOF ) x++ ;
  printf( "The number of characters: %d\n", x ) ;
  return 0 ;
}
