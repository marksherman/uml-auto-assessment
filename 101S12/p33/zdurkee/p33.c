/********************************************************/
/*  Programmer: Zachary Durkee                          */
/*                                                      */
/*  Program 33: Recursive Factorial                     */
/*                                                      */
/*  Approximate completion time:  10 minutes            */
/********************************************************/


#include <stdio.h>

int factorial( int n );

int main( int argc, char *argv[] ){

  int num, equal;

  printf( "Enter a number for the factorial: \n" );

  scanf( "%d", &num );

  equal = factorial( num );

  printf( "The factorial is:  %d\n", equal );

  return 0;

}

int factorial( int n ){

  if( n==1 || n==0 )

    return 1;

  else

    return n * factorial(n - 1);

}
