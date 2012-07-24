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

  printf( "Please enter any number:\n", x );
  scanf( "%d", &x );

  if( x == 0)
      printf( "The number is zero\n" );
    
  if( x > 0 )
      printf( "The number is positive\n" );
  
  if( x < 0 )
      printf( "The number is negative\n" );
  
    return 0;

}
