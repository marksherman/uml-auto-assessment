/*********************************************************/
/* Helen Chan                                            */
/* Assignment p6.c                                       */
/* Due February 15, 2012                                 */
/* Computing1; Mark Sherman                              */
/*********************************************************/
#include <stdio.h>

int main( )
{
  int x;
  printf("Enter a number and I'll determine whether or not it is equal to zero.\n");

  scanf("%d", &x);

  if(x<0)
    printf("The number is not equal to zero.\n");
  if(x>0)
    printf("The number is not equal to zero.\n");
  if(x==0)
    printf("The number is zero.\n");

  return 0;
}
