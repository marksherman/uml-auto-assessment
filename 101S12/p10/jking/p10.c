/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 10: Sum of 20                                          */
/* Approx Completion Time: 30 minutes                             */
/******************************************************************/

#include <stdio.h>

int main( int argc, char* argv [] ){
  
  FILE *fin; 
  int x;
  int i = 0;
  int sum;

  printf( "The sum of the 20 integers stored in testdata10 is:\n" );
  fin = fopen( "testdata10", "r" );
  for( i=0; i<20; i++ ){
    fscanf( fin, "%d", &x );
    sum = (sum + x);
}
  printf( "%d\n", sum );
  fclose( fin );  
  
  return 0;
}

