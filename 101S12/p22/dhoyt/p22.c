/*********************************/
/* Programmer: David Hoyt        */
/* Program: sum of a bunch       */
/* Tim: 10 min                   */

#include <stdio.h>

int main(){

  int x, y, z=0;

  FILE* test22;

  test22 = fopen( "testdata22", "r" );

  while( y!=EOF ){

    y = fscanf( test22, "%d", &x );

    z = x + z;

  }

  fclose( test22 );

  printf( "%d\n", z-x );

  return 0;

}
