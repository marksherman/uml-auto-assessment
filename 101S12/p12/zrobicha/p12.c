/*****************************************************************************/
/* Programmer : Zachary Robichuad                                            */
/*                                                                           */
/* Assignment : Using the sqrt function                                      */
/*                                                                           */
/* Approximate Completion Time : 5 minute                                    */
/*****************************************************************************/

#include <stdio.h>
#include <math.h>

int main() {

  float x ;
  printf( "Enter a number\n" ) ;
  scanf( "%e" , &x ) ;
  x = sqrt(x) ;
  printf( "The square root of your number is :\n%e\n" , x ) ;
  return 0 ;
}
  
