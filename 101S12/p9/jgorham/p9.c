/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 9 Scan loop                                                          */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>

int main(){
  int i;
  int nin;
  int snum = 5;
  FILE* fin;
  fin = fopen("testdata9", "r");
  for(i = 0; i < snum; i++){
    fscanf(fin, "%d", &nin);
    printf("%d ", nin);
  }
  printf("\n");
  fclose(fin);
  return 0;
}
