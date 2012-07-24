/**************************************************/
/*  Programmer: Zachary Durkee                    */
/*                                                */
/*  Program 15: Solid Box of Asterisks            */
/*                                                */
/*  Approximate completion time: 30 minutes       */
/**************************************************/

#include <stdio.h>

int main( int argc, char *argv[] )

{

  int j;

  int y;

  int i;

  int x;

  printf( "Enter the horizontal and vertical dimensions, respectively:\n" );

  scanf( "%d %d", &x, &y );

  if( x<21 && y<21 ){

    for( j=1; j<=y; j++ ){

      for( i=1; i<=x; i++ ){

	printf( "*" );

      }

      printf( "\n" );

    }

  }

  else{

    printf( "Error: x or y are equal to or larger than 21\n" );

  }

  return 0;

}
