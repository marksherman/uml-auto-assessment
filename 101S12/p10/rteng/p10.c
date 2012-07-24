/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p10.c                          */
/*                         Due: 2/17/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
int main(int argc, char* argv[])
{
  int testdata10, i, sum;
  sum=0;
  FILE *fin;
  fin = fopen("testdata10", "r");
  for(i=1; i<=20; i++){
    fscanf(fin, "%d", &testdata10);
    sum= testdata10 + sum;}
  printf("%d", sum);
  printf("\n");
  fclose(fin);
  return 0;
}
