/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p26: One Dimensional Array                    */
/*                                                       */
/* Approximate completion time: 20 minutes               */
/*********************************************************/
#include <stdio.h>
int main(int arc, char* argv[])
{
  int p26[14], i;
  /*declares p26 as an array of 15 memory locations*/
  FILE *fin;
  fin = fopen("testdata26", "r");
  /*Opens testdata26 as a readable file*/
  for(i = 0; i < 15; i++)
    {
      /*Will perform loop 15 times*/
      fscanf(fin, "%d", &p26[i]);
      /*Stores each integer into the array at the corresponding memory location of i*/
    }
  for(i = 14; i >= 0; i--)
    {
      printf("%d ", p26[i]);
      /*Will print out the integers in the array in reverse order, starting from p26[14]*/
    }
  printf("\n");
  fclose(fin);
  return 0;
}
