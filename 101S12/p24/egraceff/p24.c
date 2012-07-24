/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Find the Average                                                 */
/*                                                                           */
/* Approximate completion time: 20 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int x, i, sum;
  float average;
  FILE *fin;
  fin = fopen("testdata24", "r");
  /* opens the testdata24 file in order to read the integer values */
  fscanf(fin, "%d", &sum);
  /* saves the first integer's value to the variable sum */
  for (i=2; i<=4; i++) {
  /* runs the loop once for each of the remaining values */
    fscanf(fin, "%d", &x);
    /* saves the integer to the variable x so that it can be added to the
       cummulative sum */
    sum = sum + x;
    /* adds the integer value stored in x to the sum */
  }
  average = sum/4.0;
  /* computes the average */
  printf("The average of the four integer values is %f.\n", average);
  /* prints the average of the four integers */
  return 0 ;
}
