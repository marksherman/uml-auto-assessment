/********************************************/
/* Programmer: Kevin Southwick              */
/*                                          */
/* Program 3: Sum of Two Values             */
/*                                          */
/* Approximate completion time: 10  minutes */
/********************************************/

#include <stdio.h>

int main ( ) {

  int variable, variable2 ;

  printf ( "input two  integer values: " );

  scanf ( "%d %d" , &variable, &variable2 );
  
  variable  = variable + variable2;

   printf ( "sum is: %d \n" , variable );

  return 0;

}
