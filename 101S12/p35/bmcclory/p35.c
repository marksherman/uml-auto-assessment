/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #35: Passing a Two-Dimensional Array          */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int sum(int values[3][3], int total); /* to distinguish it from the function name */

  int main(int argc, char* argv[]){

  int values[3][3];

  int total, i;

  for(i = 0; i < 9; i++){
    printf("Type an integer: ");
    scanf("%d", values[i]);
  }

  int sum(int values[][3], int total);

  printf("%d", total);

  return 0;
}

int sum(int values[][3], int total){

  int i;

  for(i = 0; i <= 9; i++){
    total += values[i];
  }

  return total;
}
