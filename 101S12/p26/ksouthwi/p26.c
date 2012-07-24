/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 26: One Dimensional Array          */
/*                                            */
/* Approximate completion time: 20  minutes   */
/**********************************************/

#include <stdio.h>

int main( int argc , char* argv[] ) {
  
  int i, x[15] ;
  
  FILE *fin;
  
  fin = fopen( "testdata26" , "r" );
  
  for( i = 0 ; i < 15 ; i ++ )
    
    fscanf ( fin ,  "%d" , &x[i] );
  
  for( i = 14 ; i >= 0 ; i-- )

    printf( "%d " , x[i] );
  
  printf("\n");
  
  fclose( fin );
  
  return 0;
  
}
