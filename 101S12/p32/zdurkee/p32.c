/******************************************************/
/*  Programmer: Zachary Durkee                        */
/*                                                    */
/*  Program 32: Non-recursive Factorial               */
/*                                                    */
/*  Approximate completion time: 30 minutes           */
/******************************************************/

#include <stdio.h>

int factorial( int n );

int main( int argc, char *argv[] ){

  int num, equal;

  printf( "Enter number to perform a factorial on: \n" );

  scanf( "%d", &num );

  equal = factorial( num );

  printf( "The factorial is:  %d\n", equal );

  return 0;

}

int factorial( int n ){

  int i, product = 1;

  for( i=0; i<n; i++ )

    product = product * (n - i);

  return product;

}
