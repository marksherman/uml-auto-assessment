/*********************************************************/
/* Helen Chan                                            */
/* Assignment p5.c                                       */
/* Due February 13, 2012                                 */
/* Computing1; Mark Sherman                              */
/*********************************************************/
#include <stdio.h>

int main( )
{

  int x;
  printf("Input a number and I'll determine whether or not it is greater than or less than 100.\n");

  scanf("%d", &x);

  if(x<100)
    printf("The number is not bigger than 100.\n");
  if(x>100)
    printf("The number is bigger than 100.\n");
  if(x==100)
    printf("The number is 100.\n");

  return 0;
}

