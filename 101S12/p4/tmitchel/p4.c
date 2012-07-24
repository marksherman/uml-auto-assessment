/* Thomas Mitchell p4.c 2/12/12 */


#include <stdio.h>

int main ()
{

  FILE *fin;
  int x;
  fin = fopen ("testdata4.txt" , "r");
  fscanf(fin, "%d" , &x);
  printf ("%d\n", x);

  fclose(fin);

  return 0;
}
