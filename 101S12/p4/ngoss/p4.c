/********************************************************/
/* Programmer: Nathan Goss                              */
/*                                                      */
/* Program 4: The fscanf Function                       */
/*                                                      */
/* Approximate completion time: 3 minutes               */
/********************************************************/

#include <stdio.h>

int main()
{
  FILE* fin;
  int val;

  fin = fopen("testdata4","r");
  fscanf(fin, "%d", &val);
  printf("%d\n", val);

  return 0;

}
