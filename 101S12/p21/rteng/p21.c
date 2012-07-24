/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p21: scanf returns what?                      */
/*                                                       */
/* Approximate completion time: 10 minutes               */
/*********************************************************/
#include <stdio.h>
int main(int arc, char* argv[])
{
  int testdata21;
  FILE *fin;
  fin = fopen("testdata21", "r");
  while (fscanf (fin, "%d", &testdata21) != EOF)
    printf("%d ", testdata21);
  printf("\n");
  fclose(fin);
  return 0;
}
