/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 30: Simulating Call By Reference         */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

void swap( int *z, int *m );
int main( int argc, char* argv[] ){
  
  int x = 0;
  int y = 0;

  printf("\nEnter an interger value for X:\n");
  scanf("%d", &x);

  printf("\nEnter another interger value for Y:\n");
  scanf("%d", &y);

  swap( &x, &y );

  printf("\nThe swapped values are X: %d and Y: %d\n", x, y);
  
  return 0;

}

void swap( int *z, int *m ){

  int x = 0;

  x = *z;
  *z = *m;
  *m = x;

  return;

}
