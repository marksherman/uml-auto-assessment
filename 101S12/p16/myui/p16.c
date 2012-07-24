/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Count Characters                 */
/*                                            */
/* Approximate completion time:               */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int check, i;

  printf( "Enter a phrase: " );

  while ( check != EOF ) {
    
    check = getchar( );
    
    if ( check != EOF ) {
      
      i++;
    }

  }

  printf( "\nKey Entered: %d\n", i );

  return 0;
}
