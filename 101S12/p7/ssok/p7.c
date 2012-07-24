/*Programmer: Scott Sok*/

/*Program p7: Positive, Negative, or Zero?*/

/*Approximate completion time: 15 minutes*/

#include <stdio.h>

int main(){

  int number;

  printf( "Enter a number:\n", number );

  scanf( "%d", &number );

  if( number == 0 ){

    printf( "The number is zero\n" );
  } 
  if( number > 0 ){
    
    printf( "The number is positive\n" );
  }
  if( number < 0 ){

    printf( "The number is negative\n" );
    }
  return 0;

}

