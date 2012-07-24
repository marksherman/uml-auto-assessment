/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p4.c                           */
/*                         Due: 2/13/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
int main()
{
  int testdata4;
  FILE *fin;
  fin = fopen("testdata4", "r");
  fscanf(fin, "%d", &testdata4);
  printf("The integer in testdata4 is \n");
  printf("%d\n", testdata4);
  fclose(fin);
  return 0;
}
