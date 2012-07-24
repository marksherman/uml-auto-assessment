/********************************/
/* Programmer:David Hoyt        */
/* Program: scnaf returns?      */
/* Time: 20min                  */

#include <stdio.h>

int main(){

  int x = 0, y;
  
  FILE* test21;

  test21 = fopen( "testdata21", "r" );
  
  while( y!=EOF ){

   y = fscanf( test21, "%d", &x );

   printf( "%d\n", x );

  }
  
  fclose( test21 );

  return 0;

}
