/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 2 : The sum of two values       */
/*                                         */
/* Approximate completion time: 60 minutes */
/*******************************************/

#include <stdio.h>
int  main ()
{
  int a, b, sum;
  
  printf ("Enter numbers below:\n");
  
  scanf ("%d%d", &a, &b);

  sum = a + b;
  
  printf ("The sum of the numbers is:%d\n", sum);
 
  return 0;
}
