/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Unfilled Box                     */
/*                                            */
/* Approximate completion time: 15 minutes     */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {
  
  int i, j, L, H;
  
  L = 0;
  H = 0;

  printf( "Please enter length and height: " );
  scanf( "%d%d", &L, &H );

  for( i = 0; i < H; i++ ) {
    
    if ( ( i != 0 ) && ( i != H -1 ) ){ 
      
      for( j = 0; j < L; j++ ) {
      
	if ( ( j != 0 ) && ( j != L - 1 ) )
	  printf( " " );
	else
	  printf( "*" );

      }
    
    }
    else
      
      for ( j = 0; j < L; j++ ) {
	printf( "*" );
      }

    printf( "\n" );

  }

  return 0;
}
