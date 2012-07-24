/*****************************************************/
/* Programmer: Kyle White                            */
/* Program 7: Positive, Negative, or Zero?           */
/* Approximate Completion Time: 10 minutes           */
/*                                                   */
/*****************************************************/

#include <stdio.h>

int main ()

{

  int x;

  printf( "\nEnter a number: " );

  scanf( "%d", &x);

  if (x < 0){

    printf( "The number is negative\n\n" );

  }

  else if (x==0) {

      printf( "The number is zero\n\n" );

    }

  else {

    printf( "The number is positive\n\n" );

  }

  return 0;

}
