/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Recursive  Persistence           */
/*                                            */
/* Approximate completion time: 20 minutes    */
/**********************************************/

#include<stdio.h>

int multiplier( int num );

int persistencecheck( int num );

int main( int argc, char *argv[] ) {

  int num, stack;
  
  printf( "Enter a number: " );
  
  if ( scanf( "%d", &num ) != EOF ) { 
    stack = persistencecheck( num );
    printf( "The persistence is %d.\n", stack );
  }

  return 0;
}

int persistencecheck( int num ) {

  int ans;

  ans = multiplier( num );
  
  if ( ans < 9 )
    return 1;
  else { 
    return persistencecheck( ans ) + 1;
  }

}

int multiplier( int num ) {

  int lastdigit, rest;

  if ( num < 9 )
    return num;
  else {
    lastdigit = num % 10;
    rest = num / 10;
    return ( multiplier( rest ) * lastdigit );
  }

}

