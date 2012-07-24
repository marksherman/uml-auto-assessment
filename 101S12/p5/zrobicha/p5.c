/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Bigger that 100?                                             */
/*                                                                           */
/* Approximate Completion Time : 15 minutes                                  */
/*****************************************************************************/

#include <stdio.h>

int main() {

  int x ;
  
  printf( "Please input a number\n" ) ;
  scanf( "%d" , &x ) ;
  if( x >= 100 ) {
      printf( "The number is bigger than 100\n" ) ; 
  }  
  else {
      printf( "The number is not bigger than 100\n" ) ;
  }
  return 0 ;
  }
