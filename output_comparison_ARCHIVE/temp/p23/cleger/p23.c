/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: fgetc and toupper                 */
/*                                              */
/*     Time to Completion: 35 mins              */
/*                                              */
/************************************************/

#include<stdio.h>

#include<ctype.h>

int main( int argc, char *argv[] ){

  FILE *fin;

  int c;

  fin = fopen( "testdata23", "r" );
  

  while( c != EOF ){
    
    c = fgetc( fin );
    
    if( ( c >= 65 && c <= 90 ) || ( c >= 97 && c <= 122 ) )
      
      putchar( toupper( c ) );
  }
  putchar( '\n' );

  fclose( fin );

  return 0;

}
