/******************************************************************/
/* Programmer: Yasutoshi Nakamura                                 */
/*                                                                */
/* Program 4: The fscanf Function                                 */
/*                                                                */
/* Approximate completion time: 10 minutes                        */
/******************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int value;

  FILE* fin;

  fin = fopen( "testdata4", "r" );

  fscanf( fin, "%d", &value );

  printf( "The number %d.\n", value );

  fclose( fin );

  return 0;

}
