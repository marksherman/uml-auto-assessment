/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Using a for Loop                                                 */
/*                                                                           */
/* Approximate completion time: 20                                           */
/*****************************************************************************/

#include <stdio.h>
int main ( int argc, char *argv[] )
{
  int x;
  int i;
  FILE *fin;
  fin = fopen("testdata9", "r");
  /* opens the testdata9 file in order to read the integer values */
  for(i=1;i<=5;i++) {
    /* the for loop scans each value into the variable x and prints 
       the value of x for each of the integers in testdata9*/
    fscanf(fin, "%d", &x);
    printf("%d ", x);
  }
  printf("\n");
  fclose (fin);
  return 0;
}
