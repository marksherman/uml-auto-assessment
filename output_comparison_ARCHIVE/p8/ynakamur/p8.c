/***************************************************************/
/* Programmer: Yasutoshi Nakamura                              */
/*                                                             */
/* Program 8: One Horizontal Line of Asterisks                 */
/*                                                             */
/* Approximate completion time: 15 minutes                     */
/***************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int value, i;

  FILE* fin;

  fin = fopen( "testdata8", "r" );

  fscanf( fin, "%d", &value );

  for( i = 0; i < value; i++ ) {
    printf( "%c", '*' );
  }

  printf( "\n" );

  fclose( fin );

  return 0;

}
