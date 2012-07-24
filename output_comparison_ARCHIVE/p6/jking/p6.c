/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 6: Equal to Zero?                                      */
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
  else {
    printf(  "The number you entered is not equal to zero\n" );
  }
  return 0;
}

