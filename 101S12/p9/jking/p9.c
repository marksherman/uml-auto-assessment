/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 9: Using a for Loop                                    */
/* Approx Completion Time: 30 minutes                             */
/******************************************************************/

#include <stdio.h>

int main( int argc, char* argv [] ){
  
  FILE *fin; 
  int x;
  int i = 0;
  
  printf( "The five integers stored in testdata9 are:\n" );
  fin = fopen( "testdata9", "r" );
  for( i=0; i<5; i++ ){
    fscanf( fin, "%d", &x );
    printf( "%d", x );
    printf( " " );
} 
  fclose( fin ); 
  printf( "\n" ); 
  
  return 0;
}

