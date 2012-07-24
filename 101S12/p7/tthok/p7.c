/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 7 : Positive, Negative, or Zero */
/*                                         */
/* Approximate completion time: 60 minutes */
/*******************************************/

#include <stdio.h>
int  main ()
{
  int num;
  printf ("Enter a number: ");
  scanf ("%d", &num);
    if(num ==0)
      {
	printf ("The number is equal to zero.\n");
      }
    else if(num<0) 
      {
	printf ("The number is negative.\n");
      }
    else
      {
	printf ("The number is positive.\n");
      }
  return 0;
}
