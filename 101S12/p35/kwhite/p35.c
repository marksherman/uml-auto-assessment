/************************************************/
/* Programmer: Kyle White                       */
/* Program  35: Passing a Two dimensional Array */
/* Approximate completion time: 20 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int sum (int array[3][3]);
int main (int argc, char* argv [])

{

  int i=0,x=0,y=0,a=0;
  int array[3][3];

  printf( "\nEnter 9 integers to find the sum of them: " );

  for ( i=0 ; i<3 ; i++ ){

    for ( a=0 ; a<3 ; a++ ){

      scanf("%d", &x);

      array[i][a]=x;

    }

  }

  y = sum ( array );

  printf( "The sum of the 9 integers is: %d\n\n", y );

  return 0;

}

int sum ( int array[3][3] ) 

{

  int i=0,sum=0,a=0;

  for ( i=0 ; i<3 ; i++ ){

    for ( a=0 ; a<3 ; a++ )

    sum+=array[i][a];

  }

  return sum;

}
