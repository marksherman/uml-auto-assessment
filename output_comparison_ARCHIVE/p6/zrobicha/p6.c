/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Equal to Zero?                                               */
/*                                                                           */
/* Approxiamte Completion Time : 5 minutes                                   */
/*****************************************************************************/


#include <stdio.h>

int main() {

  int x ;
  printf( "Please enter a number :\n" ) ;
  scanf( "%d" , &x ) ;
  if ( x == 0 ) {
    printf( "The number is equal to zero.\n" ) ;
    }
  else {
    printf( "The number is not equal to zero.\n" ) ;
    }
  return 0 ;
}
