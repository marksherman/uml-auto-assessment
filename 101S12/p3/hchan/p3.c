/******************************************************************/
/*                                                                */
/* Helen Chan                                                     */
/* Assignment p3.c                                                */
/* Due February 10, 2012                                          */
/* Computing 1; Mark Sherman                                      */
/*                                                                */
/******************************************************************/

#include <stdio.h>
int main()
{
  int h, s, c;
  printf("Enter two numbers with a space:\n");
  scanf("%d %d", &h, &s);
  c=h+s;
  printf("Two numbers added together equals:\n");
  printf("%d\n", c);
  return 0;
}
