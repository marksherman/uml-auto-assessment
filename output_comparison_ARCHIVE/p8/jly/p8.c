/****************************************************************************/
/* Jennifer Ly                                                              */
/* p8.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include <stdio.h>
int main (int argc, char* argv[])
{ 
  int x, j;
  FILE* fin;
  fin = fopen ("testdata8", "r");
  fscanf(fin, "%d", &x);
  for(j=1; j<=x; j++){
    printf("*");}
  printf("\n");
  fclose(fin);
  return 0;
}
