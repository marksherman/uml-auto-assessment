/************************************************/
/* Programmer: Kyle White                       */
/* Program  33: Recursive Factorial             */
/* Approximate completion time: 10 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int factfind ( int a );
int main (int argc, char* argv [])

{

  int x=0,y=0;

  printf( "\nEnter a positive integer to find it's factorial: " );

  scanf( "%d", &x );

  y = factfind ( x );

  printf( "%d! = %d\n\n", x , y );

  return 0;

}

int factfind ( int a )

{

  if ( a == 1 )

    return 1;

  else
 
    return a * factfind( a-1 );

}
