/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 23                                                                   */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int in = 0;
  float avg = 0;
  FILE* fin = fopen("testdata24","r");
  while(fscanf(fin,"%d",&in)!= EOF){
    avg = avg + in;
  }
  avg = avg / 4;
  printf("Average = %f\n", avg);
  fclose(fin);
  return 0;
}
