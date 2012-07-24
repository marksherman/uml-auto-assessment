/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 5 : Bigger than 100             */
/*                                         */
/* Approximate completion time: 90 minutes */
/*******************************************/

#include <stdio.h>
int  main ()
{
  int num;
  printf ("Enter a number:");
  scanf ("%d", &num);
  if(num>100)
    {
      printf ("The number is greater than 100\n" );
    }
  if(num<=100)
    {
      printf ("The number is not bigger than 100\n");
    }
  return 0;
}
