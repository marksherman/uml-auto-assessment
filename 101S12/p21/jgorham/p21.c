/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 21 fscaf                                                             */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int in = 0;
  FILE* fin = fopen("testdata21.txt","r");
  while(fscanf(fin, "%d", &in) != EOF)
    printf("%d\n", in);
  fclose(fin);
  return 0;
}
