/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : The abs function                                             */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>            

int main() {

  int x ;
  printf( "Please enter a number :\n" ) ; 
  scanf( "%d" , &x ) ;
  x = abs(x) ;
  printf( "The absolute value of your number is :\n%d\n" , x ) ;
  return 0 ; 
}
