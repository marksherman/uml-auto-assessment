/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 20: Reverse the Command Line                           */
/* Approx Completion Time: 10 minutes                             */
/******************************************************************/

#include <stdio.h>

int main( int argc, char* argv [] ){
 
  int i = argc;
 
  for( i=argc-1 ; i>=0 ; i-- ){
    printf( argv[i] );
    printf( "\n" ); 
}
  return 0;
}

