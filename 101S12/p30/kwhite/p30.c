/************************************************/
/* Programmer: Kyle White                       */
/* Program  30: Simulating Call By Reference    */
/* Approximate completion time: 10 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

void swap( int*a, int*b );
int main (int argc, char* argv [])

{

  int x=0,y=0;

  printf( "Enter two values to be swapped: " );

  scanf( "%d", &x );

  scanf( "%d", &y );

  swap ( &x , &y );

  putchar ('\n');

  printf( "%d ", x );

  printf( "%d \n", y );

  putchar ('\n');

  return 0;

}

void swap( int*a, int*b )

{

  int tmp=0;

  tmp = *a;

  *a = *b;

  *b = tmp;

  return;

}
