/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 8 Line of asterisks                                                  */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>

int main(){
  int i;
  int anum;
  FILE* fin;
  fin = fopen("testdata8", "r");
  fscanf(fin, "%d", &anum);
  for(i = 0; i < anum; i++)
    printf("*");
  printf("\n");
  fclose(fin);
  return 0;
}
