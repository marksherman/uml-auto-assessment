/*****************************************************/
/* Programmer: Kyle White                            */
/* Program 3: Sum of Two Values                      */
/* Approximte Completion Time:                       */
/*                                                   */
/*****************************************************/



#include <stdio.h>

int main ()

{

  int x;
  int y;
  int sum;

  printf( "\nEnter two numbers:\n" );

  scanf( "%d %d", &x, &y);

  sum = x + y;

  printf( "The sum of the two numbers is:\n%d\n\n", sum);

  return 0;

}
