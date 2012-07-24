/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 32: Non-recursive Factorial             */
/*  Aproximate Completion time: 15 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>


int main( int argc, char *argv[] ) {

  /* Note: Using Float to allow for higher number factorials */

  float num = 0 ;
  float i = 0 ; 
  float sum = 1 ;

  /* Prompting user and scanning for !num */

  printf( "Enter a number for a factorial ( !x ):\n" ) ;
  scanf( "%f" , &num ) ;

  /* Using a for loop for a non recursive factorial (sum = 1) */

  for( i = num ; i > 0 ; i-- ) {
    sum = sum * i ;
  }

  /* Printing the result:  !num = sum    */

  printf( "The factorial: !%f = %f\n" , num , sum  ) ;

  return 0 ; 

}
  

