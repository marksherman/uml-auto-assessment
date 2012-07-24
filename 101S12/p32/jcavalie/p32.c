/***********************************/
/*Programmer: John Cavalieri	   */
/* Program :non recursive factorial*/
/*Completion time:	10min	   */
/***********************************/

#include<stdio.h>


int fact( int n );

int main( int argc , char* argv[] ){

  int y,x;

  printf( "Enter any integer >= zero\n " );

  scanf( "%d", &x );

  y = fact( x );

  printf( "Factorial of %d is: %d\n", x, y);

  return 0;

}

int fact( int n ){
  
  int i, f = 1;

  if ( n == 0 || n == 1 )
    return 1;

  for ( i = n ; i >= 1 ; i-- )
    f *= i;

  return f;
}
