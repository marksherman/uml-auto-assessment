/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 4 read from file                                                     */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>

int main(){
  FILE* fin;
  int din;
  fin = fopen("testdata4", "r");
  fscanf(fin, "%d", &din);
  fclose(fin);
  printf("Test data = %d\n", din);
  return 0;
}
