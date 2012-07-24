/************************************************/
/* Programmer: Kyle White                       */
/* Program  32: Non-recursive factorial         */
/* Approximate completion time: 10 Minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int factfind ( int a );
int main (int argc, char* argv [])

{

  int x=0,y=0;

  printf( "\nEnter a positive integer to find out the value it's factorial: ");

  scanf( "%d", &x );

  y = factfind (x);

  printf( "%d! =  %d\n\n", x , y );

  return 0;

}

int factfind ( int a )

{

  int i=1;
  int x=1;

  for ( i=1 ; i<=a ; i++ ){

    x = x * i;

  }

  return x;

}
