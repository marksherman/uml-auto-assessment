/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #24: Find the Average                         */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int i;

  double average;
  
  int testdata24;

  FILE *read;
  
  read = fopen("testdata24", "r");
 
  for(i = 0; i < 4; i++){

    fscanf(read, "%d", &testdata24); /* open the file and start reading */

    average += testdata24;

  }
  
  average = average / 4;
  
  printf("%f\n", average);

  fclose(read); /* The loop has finished running. Time to close the file */

  return 0;
}
