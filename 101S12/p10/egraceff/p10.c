/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Sum of Twenty                                                    */
/*                                                                           */
/* Approximate completion time: 40                                           */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int x;
  int sum;
  int i;
  FILE *fin;
  fin = fopen("testdata10", "r");
  /* opens the testdata10 file in order to read the integer values */
  fscanf(fin, "%d", &sum);
  /* saves the first integer's value to the variable sum */
  for(i=1; i<=19; i++) {
  /* runs the loop once for each of the remaining 19 integers*/
    fscanf(fin, "%d", &x);
    /* saves the integer to the variable x so that it can be added 
       to the cummulative sum */
    sum = sum + x;
    /* adds the integer value stored in x to the sum */
  }
  printf("%d\n", sum);
  /* prints the final sum of all of the integers 
     in testdata10 after the loop is exited */
  fclose(fin);
  return 0 ;
}
