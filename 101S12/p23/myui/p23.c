/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : fgetc and to upper               */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>
#include<ctype.h>

int main( int argc, char *argv[] ) {

  char input;
  FILE *scanfile;

  scanfile = fopen( "testdata23", "r" );

  while ( ( input = fgetc( scanfile )) && ( input != EOF ) ) {
        
      input = toupper( input );
      printf( "%c", input );
    
  }
  
  fclose( scanfile );

  return 0;
}
