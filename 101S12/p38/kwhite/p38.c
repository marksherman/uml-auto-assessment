/************************************************/
/* Programmer: Kyle White                       */
/* Program  38: Recursive Digit Sum             */
/* Approximate completion time: 10 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int digitsum ( int number );
int main (int argc, char* argv [])

{

  int x=0,y=0;
  FILE *fin;

  fin = fopen ( argv[1], "r" );

  while ( fscanf( fin , "%d" , &x ) != EOF ){

    printf( "\nThe number whose digits are being added is: %d\n", x );

    y = digitsum ( x );

    printf( "The sum of the digits of %d is: %d\n" , x , y );

  }

  fclose ( fin );

  return 0;

}

int digitsum ( int number )

{

  int x=0;

  if ( number == 0 )

    x = number;

  else 
    
    x = ( number % 10 ) + digitsum ( number/10 );

  return x;

}
