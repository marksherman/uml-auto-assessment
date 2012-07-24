/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Reverse                                                      */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/


#include <stdio.h>

int main( int argc , char* argv[] ) {

  int nums[10] , i ;

  for( i = 0 ; i < 10 ; i++ ) {
    printf( "Enter number %d in the series to be reversed\n" , i + 1 ) ;
    scanf( "%d" , &nums[i] ) ;
  }
  printf( "\n" ) ;
  for( i = 9 ; i >=0 ; i-- ) 
    printf( "%d" , nums[i] ) ; 
  printf( "\n\n" ) ;
  return 0 ; 
}
