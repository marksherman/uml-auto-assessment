/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 30:   Simulating Call By Reference           */
/* Time:         5 minutes                             */
/********************************************************/
#include <stdio.h>

void swap( int *a, int *b ) ;
 
int main( int argc, char *argv[] ) {
 
  int x, y ;
 
  x = atoi( argv[1] ) ;
  y = atoi( argv[2] ) ;

  swap( &x , &y ) ;
  printf("\n%d %d\n\n",x , y) ;
  return 0 ;
 
}
 
void swap( int *a, int *b ) {

  int temp ;
 
  temp  = *a  ;
  *a  =  *b  ;
  *b  =  temp ;
 
  return ;
}
