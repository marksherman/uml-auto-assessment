/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: scanf returns what?                                              */
/*                                                                           */
/* Approximate completion time: 20 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int x;
  FILE *fin;
  fin = fopen("testdata21", "r");
  /* opens the testdata21 file in order to read integer values */
  while (fscanf(fin, "%d", &x) !=EOF){
  /* runs the loop until EOF is reached within testdata21 */
    printf("%d ", x);
    /* prints each number as it is read */
  }
  fclose(fin);
  printf("\n");
  return 0 ;
}
