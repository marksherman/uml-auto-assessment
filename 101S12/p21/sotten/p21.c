/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 21: scanf returns what? */
/*                                */
/*Approx. Completion Time: 30mins */
/*                                */
/**********************************/

#include <stdio.h>

int main(int argc, char*argv[]){

  int number;

  FILE*fin;

  fin=fopen("testdata21","r"); /*opens and reads contents of file testdata21*/ 
  
  printf("The Unknown number in testdata21 is:\n");

  while(fscanf(fin, "%d", &number)!=EOF){ /* use of the boolean expression which returns a number and does not equal an EOF */

    printf("%d\n", number); /* print as a decimal*/
  }

  fclose(fin); /*closes file*/

  return 0;

  }

