/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: The fscanf Function                                              */
/*                                                                           */
/* Approximate completion time: 25                                           */
/*****************************************************************************/

#include <stdio.h>
int main (){
  int x;
  FILE *fin;
  fin = fopen("testdata4", "r");
  fscanf(fin, "%d", &x);
  printf("\nThe single integer value in the testdata4 file is %d. \n\n", x);
  fclose(fin);
  return 0;
}
