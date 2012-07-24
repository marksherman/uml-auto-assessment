/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 2: The scanf Function                    */
/*                                                  */
/* Approximate completion time:   10 Minutes        */
/****************************************************/

#include <stdio.h>

int main () {

  int val;

  printf( "Enter A Number:\n");
  scanf( "%d", &val );   
  printf( "\nYour Number is %d\n", val );
  
  val = 0;

  return 0;

}

