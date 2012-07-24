/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 33:   Recursive Factorial                    */
/* Time:         15 minutes                             */
/********************************************************/
#include <stdio.h>
int factorial ( int n );
int main ( int argc, char*argv[] ) {
  int n , y;
  printf ( "\nPlease enter an integer:\n" ) ;
  scanf ( "%d", &n );
  y = factorial( n );
  printf ( "The factorial of %d is %d", n, y ) ;
  printf ( "\n\n" ) ;  
  return 0 ;
}

int factorial ( int n ) {
  int result = 1;
  while (n > 1)
    {
      result = n*factorial( n-1 );
      return result;
    }
  return result ;
}
