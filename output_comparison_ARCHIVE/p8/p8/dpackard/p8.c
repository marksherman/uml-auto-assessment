/***Danny Packard***/
/*** p8 asterisks**/
/*** 20 minutes***/
#include<stdio.h>
int main(){
  int x;
  int v;
  FILE*fin;
  fin=fopen("testdata8","r");
  fscanf(fin,"%d",&x);
  for(v=0;v<x;v++){
    printf("*");
  }
  printf("\n");
  fclose(fin);
  return 0;
}
