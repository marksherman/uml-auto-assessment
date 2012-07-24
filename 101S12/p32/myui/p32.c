/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Non-recursive Factorial          */
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

  int i, sum;

  sum = 1;
  
  for( i = 1; i <= number; i++ )
    sum = sum * i;

  return sum;

}
