/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Square Deal                                                  */
/*                                                                       */
/* Approximate completion time: 6 hours                                  */
/*************************************************************************/
#include <stdio.h>
#include <stdlib.h>

int main( int argc, char *argv[] ) {
  
  int IsPrime( int x ) ;
  
  void square( int x, int z, int *A, int move1, int move2, int w ) ;
  
  int n, x, *A, i, count = 0 ;
  
    printf( "Please insert the size of your square: " ) ;
    scanf( "%d", &n ) ;
    
    A = ( int* ) malloc( sizeof( int ) * ( n * n ) ) ;
    
    printf( "Please insert your starting integer: " ) ;
    scanf( "%d", &x ) ;
    
    square( ( ( n * n ) / 2 ), x, A, 1, 2, n );
    
    for( i = 0; i < ( n * n ); i++ ) {
      if( IsPrime( A[i] ) ) printf( " %.3d", A[i] ) ; else printf( " ***"  ) ;
      count++ ;
      if( count == n ) {
        printf( "\n" ) ;
        count = 0 ;
      }
    }
    
    return 0 ;
}

  void square( int x, int z, int *A, int move1, int move2, int w ) {
    
    int i ;
    
    for( i = 0; i < move1; i++ ) {
      A[x] = z ;
      z++ ;
      x++ ;
    }
    

    for( i = 0; i < move1; i++ ) {
      A[x] = z ;
      z++ ;
      x -= w ;
    }
    
    for( i = 0; i < move2; i++ ) {
      A[x] = z ;
      z++ ;
      x-- ;
    }
    
    for( i = 0; i < move2; i++ ) {
      A[x] = z ;
      z++ ;
      x += w ;
    }
    
    if( move2 == ( w - 1) )
      for( i = 0; i < move2; i++ ) {
	A[x] = z ;
	z++ ;
	x++ ;
      }
    
    if( move2 == ( w - 1) ) return ;

    square( x, z, A, ( move1 + 2 ), ( move2 + 2 ), w ) ;
    
  }

int IsPrime( int x ) {
  
  int i         ;
  int count = 0 ;
  
  for( i = 1; i <= x; i++ ) if( x % i == 0 ) count++;
  
  return( ( count == 2 ) );
}
