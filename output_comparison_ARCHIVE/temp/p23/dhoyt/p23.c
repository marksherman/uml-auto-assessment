/**************************/
/* Programmer: David Hoyt */
/* Program: fgetc         */
/* Time: 20min            */

#include <stdio.h>
#include <ctype.h>

int main(){

  int x;

  FILE* test23;

  test23 = fopen( "testdata23", "r" );

  while( x != EOF ){

    x = toupper( fgetc( test23 ) );

    putchar( x );

  }
  
  putchar( '\n' );

  return 0;

} 
