/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #4: The fscanf Function                       */
/*                                                       */
/* Approximate Completion Time: 60 Minutes               */
/*********************************************************/

#include <stdio.h>

int main()
{

  int testdata4;

  FILE *read;

  read = fopen("testdata4", "r");

  fscanf(read, "%d/n", &testdata4); /* if this works, it would have returned 4 */

  fclose(read);

  return 0;
}
