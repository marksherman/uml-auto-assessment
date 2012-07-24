/***********************************************/
/* Programmer: Kevin Southwick                 */
/*                                             */
/* Program 15: Solid Box of Asterisks          */
/*                                             */
/* Approximate completion time: 15  minutes    */
/***********************************************/

#include <stdio.h>

int main ( int argc , char* argv[] ) {

  int L, H , i, j ;

  printf( "Please enter two positive integers, first height, then length.\n" );

  scanf( "%d %d" , &L , &H );
  
  for( j = 0 ; j < H ; j ++ ){

    for( i = 0 ; i < L ; i ++ )
    
      printf( "*" );

    printf( "\n" );

 }

  return 0;

}
