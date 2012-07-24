/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Sum of a Bunch                                                   */
/*                                                                           */
/* Approximate completion time: 1 hour                                       */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int x;
  int sum;
  FILE *fin;
  fin = fopen("testdata22", "r");
  /* opens the testdata22 file in order to read integer values */
  fscanf(fin, "%d", &sum);
  /* saves the first integer's value to the variable sum */
  while (fscanf(fin, "%d", &x) !=EOF) {
  /* runs the loop until EOF is reached within testdata21
  each time, the integer value is saved to the variable x so that it can be
  added to the cummulative sum*/
    sum = sum + x;
    /* adds the integer value stored in x to the sum */
  }
  printf("The sum of the integer values is %d.\n", sum);
  /* prints the final sum of all the integers after the loop is exited */
  return 0 ;
}
