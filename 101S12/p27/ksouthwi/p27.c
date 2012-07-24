/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 27: Reverse                        */
/*                                            */
/* Approximate completion time: 10  minutes   */
/**********************************************/

#include <stdio.h>

int main( int argc , char* argv[] ) {
  
  int i, x[10] ;

  printf( "input 10 integers \n" );

  for( i = 0 ; i < 10 ; i ++ )
    
    scanf( "%d" , &x[i] );
  
  for( i = 9 ; i >= 0 ; i-- )

    printf( "%d " , x[i] );
  
  printf("\n");
  
  return 0;
  
}
