/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 26                                                                   */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int i = 0;
  int input[15];
  FILE* fin = fopen("testdata26", "r");
  for(i = 0 ; i < 15 ; i++)
    fscanf(fin, "%d", &input[i]);
  for(i = 14 ; i >= 0 ; i--)
    printf("%d ", input[i]);
  printf("\n");
  fclose(fin);
  return 0;
}
