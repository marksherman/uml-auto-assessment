/*********************************************************************/
/*                                                                   */
/* Programmer: Ravy Thok                                             */
/*                                                                   */
/* Program 03 : Sum of Two Values                                    */
/*                                                                   */
/* Approximate Time: 25 minutes                                      */
/*                                                                   */
/*********************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int value1, value2, sum;

  printf( "Please Enter Two Numbers:\n" ); 

  scanf( "%d %d", &value1 , &value2 );

  sum = value1 + value2;

  printf( "\n%d is the sum of %d and %d \n", sum , value1, value2 );
 
  return 0 ;

}
