/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 19: Argv                                               */
/* Approx Completion Time: 10 minutes                             */
/******************************************************************/

#include <stdio.h>

int main( int argc, char* argv [] ){
 
  int i = 0;
  
  for( i=0 ; i<argc ; i++ ){
    printf( argv[i] );
    printf( "\n" );
}
 
  return 0;
}

