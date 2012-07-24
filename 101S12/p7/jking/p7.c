/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 7: Positive, Negative, or Zero?                        */
/* Approx Completion Time: 15 Mintues                             */
/******************************************************************/

#include<stdio.h>

int main( int argc, char* argv [] ){
 
  int x;
  
  printf( "Enter an integer:\n" );
  scanf( "%d" , &x );
  if( x == 0 ){
    printf( "The number is equal to zero\n" );
  }
  if( x > 0 ){
    printf( "The number you entered is positive\n" );     
  } 
  if( x < 0 ){
    printf( "The number you entered is negative\n" );
  }  

  return 0;
}

