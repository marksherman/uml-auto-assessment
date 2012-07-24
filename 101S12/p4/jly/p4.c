/****************************************************************************/
/* Jennifer Ly                                                              */
/* p4.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include <stdio.h>

int main(){
  FILE* fin;
  int x;
  fin = fopen ("testdata4", "r");

  fscanf(fin, "%d", &x);

  printf("\n%d\n", x);
  fclose(fin);
  return 0;
}
