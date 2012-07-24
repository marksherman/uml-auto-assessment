/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p6: if, else                   */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/

#include <stdio.h>

int main(){

  int x;

  printf( "Please enter a single integer:\n", x );
  scanf( "%d", &x );

  if( x == 0 ){
      printf( "The number is equal to 0\n" );
  }
  else{
      printf( "The number is not equal to 0\n" );
  }

  return 0;

}
