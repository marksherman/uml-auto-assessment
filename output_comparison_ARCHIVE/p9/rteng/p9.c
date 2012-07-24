/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p9.c                           */
/*                         Due: 2/17/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
int main(int argc, char* argv[])
{
  int testdata9, i;
  FILE *fin;
  fin = fopen("testdata9", "r");
  for(i=1; i<=5; i++){
    fscanf(fin, "%d", &testdata9);
    printf("%d ", testdata9);}
  printf("\n");
  fclose(fin);
  return 0;
}
