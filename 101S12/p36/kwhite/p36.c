/************************************************/
/* Programmer: Kyle White                       */
/* Program  36: Persistence of a Number         */
/* Approximate completion time: 30 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int persistence ( int num );
int main (int argc, char* argv [])
  
{

  int x=0,y=0;

  printf( "\nEnter a number or EOF: " );

  while ( scanf( "%d", &x ) != EOF ){

    y = persistence ( x );

    printf( "The persistence of %d is: %d\n", x , y );

    printf( "\nEnter a number or EOF: " );
    
  }

  putchar ('\n');

  return 0;

}

int persistence ( int num )

{

  int y=0,count=0;
  int prod=num;
  
  while ( prod > 9 ){

    prod = 1;

    while ( num != 0 ){

      y = num%10;

      prod = prod*y;

      num = num/10;

    }

    count ++;

    num = prod;

  }

  return count;

}
