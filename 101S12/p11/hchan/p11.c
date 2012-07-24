/*********************************************************/
/* Helen Chan                                            */
/* Assignment p11.c                                      */
/* Due February 20, 2012                                 */
/* Computing1; Mark Sherman                              */
/*********************************************************/
#include<stdio.h>
#include<stdlib.h>

int main( )
{

  int integer;
  int d;

  printf("Enter a number:\n");
  scanf("%d", &integer);

  d=abs(integer);

  printf("The absolute value of the number you entered is %d\n", d);

  return 0;
}
