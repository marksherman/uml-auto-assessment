/*
Jake Conaty
project 8
*/

#include <stdio.h>

int main(int argc, char* argv[]){

  int x;
  int y;
  FILE *fin;
  fin=fopen("testdata8", "r");
  fscanf(fin, "%d", &x);
    for(y=0; y<x; y++){

      printf("*");
    }
    fclose(fin);
    printf("\n");    

  return 0;
}
