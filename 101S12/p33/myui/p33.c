/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Recursive Factorial              */
/*                                            */
/* Approximate completion time: 15 minutes    */
/**********************************************/

#include<stdio.h>

int fact( int number );

int main( int argc, char *argv[] ) {

  int scan, ans;
  
  ans = 0;
  scan = 0;

  printf( "Enter a single integer: " );
  scanf( "%d", &scan );

  ans = fact( scan );

  printf( "The factorial is %d.\n", ans );

  return 0;
}

int fact( int number ) {

  if ( number != 1 )
    return number * fact( number - 1 );
  else
    return 1;

}
