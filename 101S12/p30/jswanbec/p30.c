/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 30: Simulating Call by Reference  */
/*                                           */
/* Approximate completion time: 20 minutes   */
/*********************************************/

#include <stdio.h>

int swap( );

int main( int argc, char *argv[] )
{
  int x;
  int y;
  printf("Input two integer values: ");
  scanf("%d %d" , &x , &y );
  printf("\nINPUT VALUES\nx = %d\ny = %d\n" , x , y );
  swap( &x , &y );
  printf("\nSWAPPED VALUES\nx = %d\ny = %d\n\n" , x , y );
  return 0;
}

int swap( int *x, int *y )
{
  int z = *x;
  *x = *y;
  *y = z;
  return 0;
}
