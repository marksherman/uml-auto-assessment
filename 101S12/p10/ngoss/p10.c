/*************************************************/
/* Programmer: Nathan Goss                       */
/*                                               */
/* Program 10: Sum of Twenty                     */
/*                                               */
/* Approximate completion time: 4 minutes        */
/*************************************************/


#include <stdio.h>

int main(int argc, char* argv[])
{
  int i, val, sum = 0;
  FILE* fin;

  fin = fopen("testdata10","r");

  for(i=0;i<20;i++)
    {
      fscanf(fin, "%d", &val);
      sum += val;
    }

  printf("%d\n", sum);

  return 0;
}
