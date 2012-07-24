/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 22: Sum of a Bunch                       */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  int x = 0;
  int y = 0;

  FILE* fin;

  fin = fopen( "testdata22", "r");

  while( fscanf( fin, "%d", &x ) != EOF ) {
    y = y + x;
  }
  
  printf("\nThe Sum is: %d\n", y);

  return 0;

}
