/****************************************************************************/
/* Jennifer Ly                                                              */
/* p10.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include <stdio.h>
int main (int argc, char* argv[])
{ 
  int testdata10, j, sum;
  sum=0;
  FILE* fin;
  fin = fopen ("testdata10", "r");
  for(j=1; j<=20; j++){
    fscanf(fin, "%d", &testdata10);
    sum= testdata10 + sum;}
  printf("%d", sum);
  printf("\n");
  fclose(fin);
  return 0;
}
