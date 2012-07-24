/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : One Demensional Array                                        */
/*                                                                           */
/* Approximate Completion Time : 20 minutes                                  */
/*****************************************************************************/

#include <stdio.h>

int main( int argc , char* argv[] ) {

  FILE *fin ;
  int nums[15] , i ; 
  
  fin = fopen( "testdata26" , "r" ) ; 
  for( i = 0 ; i < 15 ; i++ ) 
    fscanf( fin , "%d" , &nums[i] ) ;
  for( i = 14 ; i >= 0 ; i-- ) {
    printf( "%d" , nums[i] ) ;
    putchar ( '\n' ) ;
  }
  fclose( fin ) ; 
  return 0 ;
}
