/***********************************/
/* Programmr: David Hoyt           */
/* Program: Persistence            */
/* Time: 1 hour                    */

#include <stdio.h>
#include <stdlib.h>

int pers( int x, int per );

int main(){

  int x;

  while( x!=EOF ){

    printf( "Enter Number:" );

  scanf( "%d", &x );

  printf( "The persistence is %d\n", pers( x, 0 ) );

    }

  return 0;

}

int pers( int x, int per ){

  int i, y, z=1;
  
  if( x<10 )

  return 0;

  else{

    for( i=0; x>0; i++ ){

      y = x % 10;

      x = x / 10;

      z = z * y;

    }

    per++;

    if( z>9 ){

      return( pers( z, per ) );

  }

    else

      return per;

  }

}
