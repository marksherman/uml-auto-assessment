/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 22                                                                   */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int in = 0;
  int sum = 0;
  FILE* fin = fopen("testdata22","r");
  while(fscanf(fin,"%d",&in) != EOF){
    sum = sum + in;
  }
  printf("Sum = %d\n", sum);
  fclose(fin);
  return 0;
}
