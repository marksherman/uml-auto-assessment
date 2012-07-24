/*

Mike Begonis
Program p10

This program reads 20 variables from a file and adds their sum together.

*/


#include <stdio.h>

int main(int argc, char* argv[]){

  int x,y=0,i;
  FILE *fin;



  fin = fopen("testdata10","r");

  for(i=0;i<20;i++){
    fscanf(fin, "%d", &x);
    y=y+x;

  }
  printf("%d\n", y);
  fclose(fin);

  return 0;
}
