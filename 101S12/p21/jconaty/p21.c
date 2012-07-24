/***************************************/
/*Jake Conaty                          */
/*Project 21                           */
/***************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int x;
  FILE *fin;
  fin=fopen("testdata21", "r");

  while(fscanf(fin, "%d", &x)!=EOF){
    printf("%d\n", x);
  }

  fclose(fin);

  return 0;
}
