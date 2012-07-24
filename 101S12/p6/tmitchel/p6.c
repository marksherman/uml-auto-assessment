/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 6: Equal to zero?                       */
/*  Aproximate Completion time: 11 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

int main( int argc, char *argv[] )

{
  int x;
  
  printf( "Enter a number\n" );
  scanf( "%d", &x);
  if ( x == 0 ) {
    printf ( "The number is equal to zero\n" );
  }
  else {
	 printf ( "The number is not equal to zero\n" );
  }
  return 0 ;   
}
