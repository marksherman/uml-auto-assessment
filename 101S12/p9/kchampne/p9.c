/*************************************/
/* Name: Kyle Champney               */
/*                                   */
/* Program: p9                       */
/*                                   */
/* Estimated Completion Time: 10mins */
/*************************************/

#include <stdio.h>

int main(){

  FILE *fp;
  fp = fopen("testdata9", "r");

  int x;
  int number;

  for (x = 0; x < 5; x++)
    {
      fscanf(fp, "\n%d", &number);
      
	printf("%d\n", number);
    }

  fclose(fp);

  return 0;
}
