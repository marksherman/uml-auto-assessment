/*********************************************************/
/* Helen Chan                                            */
/* Assignment p4.c                                       */
/* Due February 13, 2012                                 */
/* Computing1; Mark Sherman                              */
/*********************************************************/

#include <stdio.h>

int main()
{
  FILE* fin;

  int x;
  fin = fopen ("testdata4", "r");

  fscanf(fin, "%d", &x);

  printf("%d\n", x);
  fclose(fin);

  return 0;
}
