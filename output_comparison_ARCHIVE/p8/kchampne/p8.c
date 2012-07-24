/*************************************/
/* Name: Kyle Champney               */
/*                                   */
/* Program: p8                       */
/*                                   */
/* Estimated Completion Time: 30 mins*/
/*************************************/

#include <stdio.h>

int main(){

  FILE *fp;
  fp = fopen("testdata8", "r");

  int x;
  int number;

  fscanf(fp, "%d", &number);

  for (x = 0; x < number; x++)
    {
      printf("*");
    }

  fclose(fp);

  printf("\n");

  return 0;
}
