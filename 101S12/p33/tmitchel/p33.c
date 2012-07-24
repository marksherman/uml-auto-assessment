/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 33: Recursive Factorial                 */
/*  Aproximate Completion time: 15 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>


float fac( float x ) ;

int main( int argc, char *argv[] ) {

  /* Note: Used float numbers because it allows for higher number factorials */

  float num = 0 ;

  /* Promting User and scanning for !num */

  printf( "Enter a number for a factorial ( !x )\n" ) ;
  scanf( "%f" , &num ) ;

  /* Calling AND Printing fac( num ) within printf */

  printf( "The factorial: !%f = %f\n" , num , fac( num ) ) ; 

  return 0 ;

}

float fac( float x ) {

  /* Base case */

  if( x == 1 ) 
    return 1;
  
  /* Combiner AND the recursive call */

  else
    return x * fac( x - 1 ) ;

}

  

