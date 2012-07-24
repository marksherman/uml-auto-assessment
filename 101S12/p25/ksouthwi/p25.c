/***********************************************/
/* Programmer: Kevin Southwick                 */
/*                                             */
/* Program 25: Unfilled Box                    */
/*                                             */
/* Approximate completion time: 25  minutes    */
/***********************************************/

#include <stdio.h>

int main( int argc , char* argv[] ) {

  int L, H , i, j ;

  printf( "Please enter two positive integers, first height, then length.\n" );

  scanf( "%d %d" , &L , &H );
  
  for( j = 0 ; j < H ; j ++ ){

    for( i = 0 ; i < L ; i ++ )

      if( (i == 0) || (i == (L - 1) ) || (j == 0) || (j == (H - 1) ) )

	printf( "*" );

      else printf( " " );

    printf( "\n" );

  }

  return 0;

}
