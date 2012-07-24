/********************************************/
/* Programmer: Kevin Southwick              */
/*                                          */
/* Program 7: Positive, Negative, or zero?  */
/*                                          */
/* Approximate completion time: 10  minutes */
/********************************************/

#include <stdio.h>

int main ( ) {

  int variable ;

  printf ( "Please input integer value: " );

  scanf ( "%d" , &variable );

  if ( variable == 0 ) {

    printf ( "The number is zero.  \n" );
 
 }
  
  else if ( variable > 0 ){

    printf ( "The number is positive. \n " );

  }

  else 

    printf ( "The number is negative. \n " );

  return 0;

}
