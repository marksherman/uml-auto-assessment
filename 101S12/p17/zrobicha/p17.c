/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Area of a Rectangle                                          */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int main( int argc , char* argv[] ) {

  float x, y ;
  printf( "Enter the length of the rectangle :\n" ) ;
  scanf ( "%f" , &x ) ;
  printf( "Enter the hight of the rectangle :\n" ) ;
  scanf ( "%f" , &y ) ;
  x = x * y ;
  printf( "The area of the rectangle is %f\n" , x ) ;
  return 0 ;
}
