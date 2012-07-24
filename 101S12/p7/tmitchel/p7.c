/**********************************************/
/* Programmer: Thomas Mitchell                */   
/* Program 7: Positive Negative or Zero?      */
/* Approximate completion time: 16 Minutes    */
/*                                            */   
/*                                            */
/**********************************************/
#include<stdio.h>

int main( int argc, char *argv[] )
{
  int x;
  
  printf ( "Enter a number\n" );
  scanf( "%d", &x );
  if ( x == 0 ){
    printf ( "The number is equal to zero\n" );
  }
  if ( x < 0 ){
    printf ( "The number is negative\n" );
  }
  if ( x > 0 ){
    printf ( "The number is positive\n" );
  }
    
  return 0;   
}
