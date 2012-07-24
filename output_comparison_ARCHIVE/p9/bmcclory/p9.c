/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #9: Using a For Loop                          */
/*                                                       */
/* Approximate Completion Time: 30 Minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[])
{

  int testdata9;

  int i;

  FILE *read;
  
  read = fopen("testdata9", "r");
 
     for(i = 0; i < 5; i++)

       if(i < 5){

	 fscanf(read, "%d", &testdata9); /* open the file and start reading */

	 printf("%d\n", testdata9); /* print the values you find */

       }

       else{

	 fclose(read); /* The loop has finished running. Time to close the file */

       }

  return 0;
}
