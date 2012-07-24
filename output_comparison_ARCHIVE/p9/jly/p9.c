/****************************************************************************/
/* Jennifer Ly                                                              */
/* p9.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include <stdio.h>
int main (int argc, char* argv[])
{ 
  int x, j;
  FILE* fin;
  fin = fopen ("testdata9", "r");
  for(j=1; j<=5; j++){
fscanf(fin, "%d", &x);
 printf("%d ", x);}
  printf("\n");
  fclose(fin);
  return 0;
}
