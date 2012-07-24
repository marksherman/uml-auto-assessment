/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: One Horizontal Line of Astericks                                 */
/*                                                                           */
/* Approximate completion time: 30                                           */
/*****************************************************************************/

#include <stdio.h>
int main ( int argc, char *argv[] )
{
  int x;
  int i;
  FILE *fin;
  fin = fopen("testdata8", "r");
  /* opens the testdata8 file in order to read integer value */
  fscanf(fin, "%d", &x);
  /* stores integer value in testdata8 file in x*/
  for (i=1;i<=x;i++)
  /* runs loop once for every integer in the test data file */
    printf("*");
    /* prints an asterick each time the loop is run in order to create one
       horizontal line of x astericks*/
  printf("\n");
  fclose(fin);
  return 0 ;
}
