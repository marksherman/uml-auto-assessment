/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: One Dimensional Array                                            */
/*                                                                           */
/* Approximate completion time: 30 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int i;
  int array[15];
  FILE *fin;
  fin = fopen("testdata26", "r");
  /* opens the testdata26 file in order to read integers */
  for(i=0;i<15;i++){
  /* loop allows for each integer to be scanned into the array */
    fscanf(fin, "%d", &array[i]);
  }
  for(i=14;i>=0;i--){
  /* loop allows for each integer to be printed to standard output starting 
     with the last integer */
    printf("%d ", array[i]);
  }
  fclose(fin);
  printf("\n");
  return 0 ;
}
