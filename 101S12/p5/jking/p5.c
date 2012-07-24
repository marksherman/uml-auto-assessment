/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 5: Bigger than 100?                                    */
/* Approx Completion Time: 15 Mintues                             */
/******************************************************************/

#include<stdio.h>

int main(){
 
  int x;
  
  printf( "Enter an integer:\n" );
  scanf( "%d" , &x );
  if( x <= 100 ){
    printf( "The number is not bigger than 100\n" );
  }
  else {
    printf(  "The number you entered is bigger than 100\n" );
  }
  return 0;
}

