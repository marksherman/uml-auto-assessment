/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p39: Recursive Persistence     */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdlib.h>
#include <stdio.h>

int Prime( int n );

int main(int argc, char* argv[]) {

  int x, i, j, k;

  int arr[50][50];

  printf( "Enter an odd integer:\n " ) ;
  scanf( "%d", &x ) ;

  printf( "Enter a starting value:\n " ) ;
  scanf( "%d", &i ) ;

  for( j = 0 ; j < x ; j++ ) {

    for( k = 0 ; k < x ; k++ ) {
      if( Prime( i++ ) == 1 )
	printf("%8d", i) ;
      else
	printf( "%8s", "***" ) ;
    }
    printf("\n");
    arr[j][k] = arr[j+1][k+1] ;
  }
  return 0;
}
int Prime( int x )
{
  int i, count = 0 ;
  for( i = 1 ; i <= x ; i++ )
    {
      if( ( x % i ) == 0 ) count++ ;
    }
  return ( count == 2 ) ;
}
