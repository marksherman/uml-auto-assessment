/*****************************/
/* Programmer: David Hoyt    */
/* Program: Bigger than 100? */
/* Completion Time:          */

#include <stdio.h>

int main(){

  int x;

  printf( "Enter a number:" );

  scanf( "%d", &x );

  if ( x > 100 )

    printf( "%d is bigger than 100!\n", x );
  
  if ( x < 100 )

    printf( "%d is less than 100!\n", x );
  
  if ( x == 100 )

   printf( "You entered 100!\n" );
    
  return 0;

}
