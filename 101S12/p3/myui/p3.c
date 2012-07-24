/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Sum of Two Values                */
/*                                            */
/* Approximate completion time: 5 Minutes     */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int n1, n2, sum;
  
  printf( "Enter two integer: ");

  scanf( "%d%d", &n1, &n2 );

  sum = n1 + n2;

  printf( "The sum is %d.\n", sum );

  return 0;
}
