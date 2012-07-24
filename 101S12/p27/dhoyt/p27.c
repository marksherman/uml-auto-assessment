/****************************/
/* Programmer: David Hoyt   */
/* Program Reverse          */
/* Time: 10min              */

#include <stdio.h>
#include <stdlib.h>

int main(){

  int x[10], i;

  for( i=0; i<10; i++ ){

    scanf( "%d", &x[i] );
  }

  for( i=9; i>-1; i-- ){

    printf( "%d\n", x[i] );

  }

  return 0;

}
