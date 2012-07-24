/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #10: Sum of Twenty                            */
/*                                                       */
/* Approximate Completion Time: 30 Minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[])
{

  int i;

  int sum = 0;

  int temp = 0;

  int testdata10;

  FILE *read;
  
  read = fopen("testdata10", "r");
 
  for(i = 0; i < 20; i++){

    fscanf(read, "%d", &testdata10); /* open the file and start reading */

    temp += testdata10; /* add the current value being read to temp */

  }

  sum = temp; /* get ready to display the sum */

  printf("%d\n", sum); /* print the sum */

  fclose(read); /* The loop has finished running. Time to close the file */

  return 0;
}
