/*************************************/
/* Name: Kyle Champney               */
/*                                   */
/* Program: p10                      */
/*                                   */
/* Estimated Completion Time: 1 hour */
/*************************************/

#include <stdio.h>

int main(){

  FILE *fp;
  fp = fopen("testdata10", "r");

  int x;
  int sum;
  int integer;
  int number;

  for (x = 0; x < 20; x++)
    {
      fscanf(fp, "\n%d", &number);

      integer = number;
      sum = integer + sum;
    }
     
  printf("The sum of the 20 integers in testdata10.txt is: %d\n", sum);

  fclose(fp);
  return 0;
}


