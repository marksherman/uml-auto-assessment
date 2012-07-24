/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 16: Count Characters                                 */
/*                                                              */
/* Approximate completion time: 30 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int number = 0;

  printf( "Please enter charcters until EOF is reached.\n" );

  while( getchar( ) != EOF ) {
    number++;
  }

  printf( "\n%d characters were entered.\n", number );
 
  return 0;

}
