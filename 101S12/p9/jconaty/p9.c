/*
Jake Conaty
project 9
*/

#include <stdio.h>

int main(int argc, char* argv[]){

  int x;
  int y;
  FILE* fin;
  fin=fopen("testdata9", "r");

  for(y=0; y<5; y++){

  fscanf(fin, "%d", &x);
  printf("%d\n", x);

  }
  fclose(fin);

  return 0;
}
