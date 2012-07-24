/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #11: The abs Function                         */
/*                                                       */
/* Approximate Completion Time: 30 Minutes               */
/*********************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[])

{
  int i, abs_i; /* so we can tell them apart */

  printf("Type any integer value: ");
  scanf("%d", &i);

  abs_i = abs(i); /* i = abs(i) did not feel right to me */

  printf("%d\n", abs_i);

  return 0;
}
