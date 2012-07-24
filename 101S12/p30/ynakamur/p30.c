/*****************************************************************************/
/* Programmer: Yasutoshi Nakamura                                            */
/*                                                                           */
/* Program 30: Simulating Call By Reference                                  */
/*                                                                           */
/* Approximate completion time: 10 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

void swap( int *a, int *b );

int main( int argc, char *argv[] ) {

  int x, y;

  printf( "Please input 2 integer values that are to be swapped.\n" );

  scanf( "%d %d", &x, &y );

  swap( &x, &y );

  printf( "\n%d %d\n", x, y );

  return 0;

}


void swap( int *a, int *b ) {

  int temp;

  temp = *a;
  *a = *b;
  *b = temp;

  return;

}
