/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 8: One Horizontal Line of Astericks                    */
/* Approx Completion Time: 30 minutes                             */
/******************************************************************/

#include <stdio.h>

int main( int argc, char* argv [] ){
  
  FILE *fin; 
  int x;
  int i;
  
  fin = fopen( "testdata8", "r" );
    fscanf( fin, "%d", &x );
  for( i = 0 ; i < x ; i++ ){
    printf( "*" );
}    
  printf( "\n" );   
  fclose( fin );
 
  return 0;
}

