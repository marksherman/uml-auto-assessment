/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p5: if, else                   */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/

#include <stdio.h>

int main(){

  int x;

  printf( "Please enter a number:\n", x );
  scanf( "%d", &x );
  
  if( x > 100 ){
    printf( "The number is bigger than 100\n" );
  }
  if( x < 100 ){
     printf( "The number is less than 100\n" );
    }
  
  return 0;

}
