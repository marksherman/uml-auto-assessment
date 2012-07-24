/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p8.c                           */
/*                         Due: 2/17/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
int main(int argc, char* argv[])
{
  int testdata8, i;
  FILE *fin;
  fin = fopen("testdata8", "r");
  fscanf(fin, "%d", &testdata8);
  for(i=1; i<=testdata8; i++){
    printf("*");}
  printf("\n");
  fclose(fin);
  return 0;
}
