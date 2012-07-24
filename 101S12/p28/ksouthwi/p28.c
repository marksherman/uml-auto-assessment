/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 28: Digit Sum                      */
/*                                            */
/* Approximate completion time: 30  minutes   */
/**********************************************/

#include <stdio.h>

int digitsum( int X );

int main( int argc , char* argv[] ) {

  int x , i = 0 ;

  FILE *fin;

  fin = fopen( argv[1] , "r" );

  while( (fscanf( fin ,  "%d" , &x )) != EOF )

    i = i + digitsum( x );

  printf( "%d \n" , i );

  fclose( fin );

  return 0;

}

int digitsum( int X ){

  int a = 0;

  for( ; ; ){

    if( X >= 10 ){

      a = a + ( X % 10 );

      X = ( X - ( X % 10 )) / 10 ;

    }

    else{

      ( a = a + X );

      break;

    }

  }

  return a;
}
