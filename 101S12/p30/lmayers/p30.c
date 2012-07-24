
/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Simulating call by reference                                 */
/*                                                                       */
/* Approximate completion time: 10 minutes                               */
/*************************************************************************/
#include <stdio.h>
void callbyswap( int *a, int *b );

int main ( int argc, char *argv[] ) {

  int x , y;

  printf("Please enter an integer value for X and Y:\n");
  scanf("%d %d", &x ,&y);

  callbyswap( &x, &y );

  printf("%d %d\n", x ,y);

  return 0;
}


void callbyswap( int *a, int *b ){

  int temp;

  temp = *a;
  *a = *b;
  *b = temp;
}
