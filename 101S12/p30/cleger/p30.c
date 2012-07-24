/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Simulating Call By Reference      */
/*                                              */
/*     Time to Completion: 15 mins              */
/*                                              */
/************************************************/

#include<stdio.h>

void swap( int *a, int *b );

int main( int argc, char *argv[] ) {

  int value1,value2;

  printf( "Type in two integer values seperated by a space:" );
  scanf( "%d %d", &value1, &value2 );

  swap( &value1, &value2 );
  printf( "The swapped values are:%d %d\n", value1, value2 );
  
  return 0;
  
}

void swap( int *a, int *b ) {
  
  int temp;
  
  temp = *a;
  *a = *b;  
  *b = temp;
}
