/*********************************************************/
/* Helen Chan                                            */
/* Assignment p7.c                                       */
/* Due February 15, 2012                                 */
/* Computing1; Mark Sherman                              */
/*********************************************************/

#include <stdio.h>

int main( )
{
  int x;
  printf("Enter a number and I'll determine whether or not it is positive, negative, or zero.\n");

  scanf("%d", &x);

  if(x<0)
    printf("The number is negative!\n");
  if(x>0)
    printf("The number is positive!\n");
  if(x==0)
    printf("The number is zero!\n");

  return 0;
}
