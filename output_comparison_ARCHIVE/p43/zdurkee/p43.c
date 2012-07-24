/***************************************************************/
/*  Programmer: Zachary Durkee                                 */
/*                                                             */
/*  Program 43: Square Deal                                    */
/*                                                             */
/*  Approximate completion time: 4 hours                       */
/***************************************************************/     

#include <stdio.h>

int arr[15][15];

int isPrime( int value );

int square( int I, int N );

int main( int argc, char *argv[] ){

  int initial_value, N, x, y;

  printf( "Input the array size now: ");

  scanf( "%d", &N );

  printf( "Input the initial value: " );

  scanf( "%d", &initial_value );

  printf( "\n" );

  square( initial_value, N );

  for( x = 0; x<N; x++ ){

    for( y = 0; y<N; y++ ){

      if( arr[x][y] == 0)

	printf( "%8s", "***" );

      else

	printf( "%8d", arr[x][y] );

    }

    printf( "\n" );

  }

  return 0;

}
 
int square( int I, int N ){

  int k = 1;

  int x = N/2;

  int y = N/2;

  while ( k<=N ){

    int s;

    for( s = 0; s<k; s++, y++, I++)

      arr[x][y] = isPrime( I );

    for( s = 0; s<k; s++, x--, I++)

      arr[x][y] = isPrime( I );

    k++;

    for( s = 0; s<k; s++, y--, I++)

      arr[x][y] = isPrime( I );

    for( s = 0; s<k; s++, x++, I++)

      arr[x][y] = isPrime( I );

    k++;

  }

  return 0;

}
 
int isPrime( int value ){

  int c;

  if( value == 1 )

    return 0;

  for( c = 2; c<value; c++ ){

    if( value%c == 0 )

      return 0;

  }

  return value;
  
}
