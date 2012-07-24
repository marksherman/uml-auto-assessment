/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 2 : The scanf Function          */
/*                                         */
/* Approximate completion time: 15 minutes */
/*******************************************/

#include <stdio.h>

int main( int argc, char *argv [] )
{
  int x;

  printf ("please enter an integer:");

  scanf ("%d", &x);
  
  printf ("the number you enter is %d\n", x);
 
  return 0;
}
