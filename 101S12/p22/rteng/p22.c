/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p22: Sum of a Bunch                           */
/*                                                       */
/* Approximate completion time: 15 minutes               */
/*********************************************************/
#include <stdio.h>
int main(int arc, char* argv[])
{
  int testdata22, sum = 0;
  FILE *fin;
  fin = fopen("testdata22", "r");
  /*Opens testdata22 as a readable file*/
  while (fscanf (fin, "%d", &testdata22) != EOF){
    /*Will perform, as long as it's not end of file*/
    sum = sum + testdata22;}
  /*Sums up previous sum with next integer*/
  printf("The sum of the integers in testdata22 is %d\n", sum);
  /*Prints the final sum*/
  fclose(fin);
  return 0;
}
