
/*
Mike Begonis
Project p8

This program reads a number from a file, and then prints out a number of asterisks that equal the stored number.


*/



#include <stdio.h>

int main(int argc, char* argv[]){

  int x;
  int y;

  FILE *fin;
  fin = fopen("testdata8","r");

  fscanf(fin, "%d", &x);

  for(y=0; y<x;y++){

    printf("*");

  }
  printf("\n");


  fclose(fin);



  return 0;
}

