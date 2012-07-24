/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : fgetc and touppper                                           */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <ctype.h>

int main( int argc , char* argv[] ) {

  
  FILE *fin ;
  char x ; 

  fin = fopen( "testdata23" , "r" ) ;
  while( ( x = fgetc( fin ) ) != EOF ) {
    putchar( toupper( x ) )  ;
  } 
  fclose( fin ) ;
  return 0 ; 

}
