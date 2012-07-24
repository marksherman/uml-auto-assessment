/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p24: Find the Average                         */
/*                                                       */
/* Approximate completion time: 14 minutes               */
/*********************************************************/
#include <stdio.h>
int main(int arc, char* argv[])
{
  int testdata24, sum = 0, i;
  double avg;
  FILE *fin;
  fin = fopen("testdata24", "r");
  /*Opens testdata24 as a readable file*/
  for (i = 1; i < 5; i++)
    {
      /*Will perform loop only 4 times*/
      fscanf(fin, "%d", &testdata24);
      sum = sum + testdata24;
    }
  /*Sums up previous sum with next integer so we get the sum of the 4 integers*/
  avg = (double) sum / 4;
  printf("The average of the four numbers in testdata24 is %lf\n", avg);
  /*Prints the average of the four numbers*/
  fclose(fin);
  return 0;
}
