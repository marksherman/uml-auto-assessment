/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Simulating Call By Reference                                 */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

void swap( int* x , int * y ) ;

int main( int argc , char* argv[] ) {

  int x , y ;

  printf( "Enter two numbers to be swapped\n" ) ;
  scanf( "%d%d" , &x , &y ) ;
  swap( &x , &y ) ;
  printf( "The first variable is now %d\nThe second variable is now %d\n" , x ,           y ) ;
  return 0 ;
}

void swap( int* x , int* y ) {
  
  int temp ;
  
  temp = *x ;
  *x = *y ;
  *y = temp ;
}
