/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #8: One Horizontal Line of Asterisks          */
/*                                                       */
/* Approximate Completion Time: 30 Minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[])
{

  int testdata8;

  int i;

  FILE *read;
  
  read = fopen("testdata8", "r");
  
  fscanf(read, "%d/n", &testdata8);

  for(i = 0; i <= testdata8; i++)

   if(i < testdata8){
      printf("*"); /* keep printing asterisks until loop has finished */
    }
   else{
      printf("\n"); /*print a newline character once the loop has finished running */
    };

 fclose(read); /* close the file */

 return 0;
}
