/*
Jake Conaty
project 10*/

#include <stdio.h>

int main(int argc, char* argv[]){

  int x;
  int y=0;
  int z;

  FILE *fin;
  fin=fopen("testdata10", "r");

  for(z=0; z<20; z++){
    fscanf(fin, "%d", &x);
    y=y+x;
  }
    printf("%d\n", y);
  return 0;
}
